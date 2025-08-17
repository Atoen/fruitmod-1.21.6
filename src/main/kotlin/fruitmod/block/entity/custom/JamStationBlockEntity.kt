package fruitmod.block.entity.custom

import fruitmod.ModTags
import fruitmod.block.custom.JamStationBlock
import fruitmod.block.entity.JamCraftingInventory
import fruitmod.block.entity.ModBlockEntities
import fruitmod.item.ModItems
import fruitmod.jam.recipe.JamRecipeKey
import fruitmod.jam.recipe.JamRecipeRegistry
import fruitmod.screen.custom.JamStationScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.block.entity.LockableContainerBlockEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.PropertyDelegate
import net.minecraft.storage.ReadView
import net.minecraft.storage.WriteView
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class JamStationBlockEntity(
    blockPos: BlockPos,
    blockState: BlockState
) : LockableContainerBlockEntity(
    ModBlockEntities.JAM_STATION,
    blockPos,
    blockState
), SidedInventory, JamCraftingInventory {

    private var inventory = DefaultedList.ofSize(7, ItemStack.EMPTY)
    private var jammingTime = 0
    private var jammingTotalTime = 0

    private val propertyDelegate: PropertyDelegate = object : PropertyDelegate {
        override fun get(index: Int) = when (index) {
            0 -> jammingTime
            1 -> jammingTotalTime
            2 -> if (isHeated) 1 else 0
            else -> 0
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> jammingTime = value
                1 -> jammingTotalTime = value
            }
        }

        override fun size() = 3
    }

    override fun size() = inventory.size
    override fun getContainerName(): Text =
        Text.translatable("container.fruitmod.jam_station")

    override fun getHeldStacks(): DefaultedList<ItemStack> = inventory
    override fun setHeldStacks(inventory: DefaultedList<ItemStack>) {
        this.inventory = inventory
    }

    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory) =
        JamStationScreenHandler(syncId, playerInventory, this, propertyDelegate)

    override fun getAvailableSlots(side: Direction) = SLOTS
    override fun canInsert(slot: Int, stack: ItemStack, dir: Direction?) =
        isValid(slot, stack)

    override fun canExtract(slot: Int, stack: ItemStack, dir: Direction?) = true

    override fun isValid(slotIndex: Int, stack: ItemStack) = when (slotIndex) {
        SLOT_JAM_BASE -> stack.isIn(ModTags.Items.JAM_BASE)
        in SLOT_INGREDIENT_1..SLOT_INGREDIENT_4 -> stack.isIn(ModTags.Items.JAM_INGREDIENT)
        SLOT_JAR -> stack.isOf(ModItems.JAR)
        else -> true
    }

    override fun readData(view: ReadView) {
        super.readData(view)
        inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY)
        Inventories.readData(view, inventory)

        jammingTime = view.getShort(JAMMING_TIME_KEY, 0)
        jammingTotalTime = view.getByte(JAMMING_TOTAL_TIME_KEY, 0).toInt()
    }

    override fun writeData(view: WriteView) {
        super.writeData(view)
        view.putShort(JAMMING_TIME_KEY, jammingTime.toShort())
        Inventories.writeData(view, inventory)
        view.putByte(JAMMING_TOTAL_TIME_KEY, jammingTotalTime.toByte())
    }

    override val baseStack get() = inventory[SLOT_JAM_BASE]
    override val ingredients get() = (SLOT_INGREDIENT_1..SLOT_INGREDIENT_4).map { inventory[it] }
    override val jarStack get() = inventory[SLOT_JAR]
    override var outputStack
        get() = inventory[SLOT_OUTPUT]
        set(value) { inventory[SLOT_OUTPUT] = value }

    private val isHeated get() = cachedState.get(JamStationBlock.HEATED)

    private var cachedCraftResult: ItemStack? = null
    private var lastInputKey: JamRecipeKey? = null

    fun updateCacheIfNeeded() {
        val currentKey = JamRecipeKey.createKey(baseStack, ingredients)
        if (currentKey != lastInputKey) {
            lastInputKey = currentKey
            cachedCraftResult = JamRecipeRegistry.craft(this)
        }
    }

    fun canCraft(): Boolean {
        updateCacheIfNeeded()
        return cachedCraftResult?.let { !it.isEmpty } ?: false
    }

    fun canAcceptOutput(maxCount: Int): Boolean {
        val result = cachedCraftResult ?: return false
        val output = outputStack
        return when {
            output.isEmpty -> true
            !ItemStack.areItemsAndComponentsEqual(output, result) -> false
            output.count < maxCount && output.count < output.maxCount -> true
            else -> output.count < result.count
        }
    }

    companion object {

        private val SLOTS = intArrayOf(0, 1, 2, 3, 4, 5, 6)

        const val SLOT_JAM_BASE = 0
        const val SLOT_INGREDIENT_1 = 1
        const val SLOT_INGREDIENT_2 = 2
        const val SLOT_INGREDIENT_3 = 3
        const val SLOT_INGREDIENT_4 = 4
        const val SLOT_JAR = 5
        const val SLOT_OUTPUT = 6

        private const val JAMMING_TIME_KEY = "JammingTime"
        private const val JAMMING_TOTAL_TIME_KEY = "JammingTotalTime"

        private fun getJammingTime(world: World, blockEntity: JamStationBlockEntity): Int {
            return 200
        }

        private fun canAcceptOutput(inv: JamCraftingInventory, maxCount: Int): Boolean {
            val result = JamRecipeRegistry.craft(inv)
            if (result.isEmpty) {
                return false
            }

            val output = inv.outputStack
            return when {
                output.isEmpty -> true
                !ItemStack.areItemsAndComponentsEqual(output, result) -> false
                output.count < maxCount && output.count < output.maxCount -> true
                else -> output.count < result.count
            }
        }

        private fun craftRecipe(inv: JamCraftingInventory, maxCount: Int): Boolean {
            if (!canAcceptOutput(inv, maxCount)) {
                return false
            }

            val result = JamRecipeRegistry.craft(inv)
            if (inv.outputStack.isEmpty) {
                inv.outputStack = result.copy()
            } else {
                inv.outputStack.increment(1)
            }

            inv.consumeIngredients()
            return true
        }

        fun tick(world: World, pos: BlockPos, state: BlockState, be: JamStationBlockEntity) {
            var dirty = false

            be.updateCacheIfNeeded()

            if (be.hasAllRequired && be.canCraft() && be.isHeated && be.canAcceptOutput(be.maxCountPerStack)) {
                be.jammingTime++
                if (be.jammingTime >= be.jammingTotalTime) {
                    be.jammingTime = 0
                    be.jammingTotalTime = getJammingTime(world, be)
                    if (craftRecipe(be, be.maxCountPerStack)) {
                        dirty = true
                    }
                }
            } else if (be.jammingTime > 0) {
                be.jammingTime = (be.jammingTime - 1).coerceAtLeast(0)
                dirty = true
            }

            if (dirty) {
                markDirty(world, pos, state)
            }
        }
    }
}
