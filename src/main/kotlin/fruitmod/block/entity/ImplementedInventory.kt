package fruitmod.block.entity

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.Direction

/**
 * A simple [SidedInventory] implementation with only default methods + an item list getter.
 * License: CC0
 */
fun interface ImplementedInventory : SidedInventory {

    /**
     * Gets the item list of this inventory.
     * Must return the same instance every time it's called.
     */
    fun getItems(): DefaultedList<ItemStack>

    companion object {
        /**
         * Creates an inventory from the item list.
         */
        @JvmStatic
        fun of(items: DefaultedList<ItemStack>): ImplementedInventory = ImplementedInventory { items }

        /**
         * Creates a new inventory with the size.
         */
        @JvmStatic
        fun ofSize(size: Int): ImplementedInventory =
            of(DefaultedList.ofSize(size, ItemStack.EMPTY))
    }

    // SidedInventory

    override fun getAvailableSlots(side: Direction?): IntArray {
        return getItems().indices.toList().toIntArray()
    }

    override fun canInsert(slot: Int, stack: ItemStack, side: Direction?): Boolean = true

    override fun canExtract(slot: Int, stack: ItemStack, side: Direction): Boolean = true

    // Inventory

    override fun size(): Int = getItems().size

    override fun isEmpty(): Boolean = getItems().all { it.isEmpty }

    override fun getStack(slot: Int): ItemStack = getItems()[slot]

    override fun removeStack(slot: Int, count: Int): ItemStack {
        val result = Inventories.splitStack(getItems(), slot, count)
        if (!result.isEmpty) markDirty()
        return result
    }

    override fun removeStack(slot: Int): ItemStack =
        Inventories.removeStack(getItems(), slot)

    override fun setStack(slot: Int, stack: ItemStack) {
        getItems()[slot] = stack
        if (stack.count > maxCountPerStack) {
            stack.count = maxCountPerStack
        }
    }

    override fun clear() {
        getItems().clear()
    }

    override fun markDirty() {
        // Override if you want behavior.
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean = true
}
