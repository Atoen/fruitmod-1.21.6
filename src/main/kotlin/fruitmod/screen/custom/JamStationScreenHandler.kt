package fruitmod.screen.custom

import fruitmod.screen.ModScreenHandlers
import fruitmod.screen.custom.slots.JamBaseSlot
import fruitmod.screen.custom.slots.JamIngredientSlot
import fruitmod.screen.custom.slots.JamJarSlot
import fruitmod.screen.custom.slots.JamOutputSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ArrayPropertyDelegate
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.MathHelper

class JamStationScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    private val inventory: Inventory,
    private val propertyDelegate: PropertyDelegate,
) : ScreenHandler(ModScreenHandlers.JAM_STATION_SCREEN_HANDLER, syncId) {

    val jammingProgress: Float get() {
        val time = propertyDelegate[0]
        val totalTime = propertyDelegate[1]

        return if (time != 0 && totalTime != 0) {
            MathHelper.clamp(time.toFloat() / totalTime, 0f, 1f)
        } else 0f
    }

    var jammingTime
        get() = propertyDelegate[0]
        private set(value) { propertyDelegate[0] = value }

    var jammingTotalTime
        get() = propertyDelegate[1]
        private set(value) { propertyDelegate[1] = value }

    val isHeated get() = propertyDelegate[2] == 1

    constructor(syncId: Int, playerInventory: PlayerInventory) : this(
        syncId,
        playerInventory,
        SimpleInventory(7),
        ArrayPropertyDelegate(3)
    )

    init {
        checkSize(inventory, 7)
        checkDataCount(propertyDelegate, 3)

        val resetProgress = {
            jammingTime = 0
            jammingTotalTime = 200
        }

        addSlot(JamBaseSlot(inventory, SLOT_JAM_BASE, 17, 35))

        addSlot(JamIngredientSlot(inventory, SLOT_INGREDIENT_1, 41, 26, resetProgress))
        addSlot(JamIngredientSlot(inventory, SLOT_INGREDIENT_2, 59, 26, resetProgress))
        addSlot(JamIngredientSlot(inventory, SLOT_INGREDIENT_3, 41, 44, resetProgress))
        addSlot(JamIngredientSlot(inventory, SLOT_INGREDIENT_4, 59, 44, resetProgress))

        addSlot(JamJarSlot(inventory, SLOT_JAR, 83, 26))

        addSlot(JamOutputSlot(playerInventory.player, inventory, SLOT_OUTPUT, 142, 35))

        addPlayerSlots(playerInventory, 8, 84)
        addProperties(propertyDelegate)
    }

    override fun quickMove(player: PlayerEntity, slotIndex: Int): ItemStack {
        var itemStack = ItemStack.EMPTY
        val slot = slots[slotIndex]

        if (slot.hasStack()) {
            val stackInSlot = slot.stack
            itemStack = stackInSlot.copy()

            when (slotIndex) {
                SLOT_OUTPUT -> { // Output slot → move to player inventory/hotbar
                    if (!insertItem(stackInSlot, FIRST_PLAYER_SLOT, LAST_HOTBAR_SLOT + 1, true)) {
                        return ItemStack.EMPTY
                    }
                    slot.onQuickTransfer(stackInSlot, itemStack)
                }

                in FIRST_PLAYER_SLOT..LAST_PLAYER_SLOT -> { // Player main inventory
                    when {
                        isJamBase(stackInSlot) -> {
                            if (!insertItem(stackInSlot, SLOT_JAM_BASE, SLOT_JAM_BASE + 1, false)) return ItemStack.EMPTY
                        }
                        isIngredient(stackInSlot) -> {
                            if (!insertItem(stackInSlot, SLOT_INGREDIENT_1, SLOT_JAR, false)) return ItemStack.EMPTY
                        }
                        isJar(stackInSlot) -> {
                            if (!insertItem(stackInSlot, SLOT_JAR, SLOT_JAR + 1, false)) return ItemStack.EMPTY
                        }
                        else -> {
                            if (!insertItem(stackInSlot, FIRST_HOTBAR_SLOT, LAST_HOTBAR_SLOT + 1, false)) return ItemStack.EMPTY
                        }
                    }
                }

                in FIRST_HOTBAR_SLOT..LAST_HOTBAR_SLOT -> { // Player hotbar
                    when {
                        isJamBase(stackInSlot) -> {
                            if (!insertItem(stackInSlot, SLOT_JAM_BASE, SLOT_JAM_BASE + 1, false)) return ItemStack.EMPTY
                        }
                        isIngredient(stackInSlot) -> {
                            if (!insertItem(stackInSlot, SLOT_INGREDIENT_1, SLOT_JAR, false)) return ItemStack.EMPTY
                        }
                        isJar(stackInSlot) -> {
                            if (!insertItem(stackInSlot, SLOT_JAR, SLOT_JAR + 1, false)) return ItemStack.EMPTY
                        }
                        else -> {
                            if (!insertItem(stackInSlot, FIRST_PLAYER_SLOT, LAST_PLAYER_SLOT + 1, false)) return ItemStack.EMPTY
                        }
                    }
                }

                in FIRST_CONTAINER_SLOT..LAST_CONTAINER_SLOT -> { // Any input slot
                    if (!insertItem(stackInSlot, FIRST_PLAYER_SLOT, LAST_HOTBAR_SLOT + 1, false)) return ItemStack.EMPTY
                }
            }

            if (stackInSlot.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }

            if (stackInSlot.count == itemStack.count) {
                return ItemStack.EMPTY
            }

            slot.onTakeItem(player, stackInSlot)
        }

        return itemStack
    }

    private fun isJamBase(stack: ItemStack) = JamBaseSlot.isValidItem(stack)
    private fun isIngredient(stack: ItemStack) = JamIngredientSlot.isValidItem(stack)
    private fun isJar(stack: ItemStack) = JamJarSlot.isValidItem(stack)

    override fun canUse(player: PlayerEntity) = inventory.canPlayerUse(player)

    companion object {
        const val SLOT_JAM_BASE = 0
        const val SLOT_INGREDIENT_1 = 1
        const val SLOT_INGREDIENT_2 = 2
        const val SLOT_INGREDIENT_3 = 3
        const val SLOT_INGREDIENT_4 = 4
        const val SLOT_JAR = 5
        const val SLOT_OUTPUT = 6

        const val FIRST_CONTAINER_SLOT = SLOT_JAM_BASE
        const val LAST_CONTAINER_SLOT = SLOT_OUTPUT

        const val FIRST_PLAYER_SLOT = LAST_CONTAINER_SLOT + 1
        const val LAST_PLAYER_SLOT = FIRST_PLAYER_SLOT + 26

        const val FIRST_HOTBAR_SLOT = LAST_PLAYER_SLOT + 1
        const val LAST_HOTBAR_SLOT = FIRST_HOTBAR_SLOT + 8
    }
}
