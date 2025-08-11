package fruitmod.screen.custom.slots

import fruitmod.ModTags
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.util.Identifier

class JamBaseSlot(
    inventory: Inventory,
    index: Int,
    x: Int,
    y: Int
) : Slot(inventory, index, x, y) {

    override fun canInsert(stack: ItemStack) = stack.isIn(ACCEPTED_TAG)

    override fun getBackgroundSprite(): Identifier = SPRITE

    companion object {
        private val SPRITE = Identifier.ofVanilla("container/slot/redstone_dust")

        val ACCEPTED_TAG = ModTags.Items.JAM_BASE

        fun isValidItem(stack: ItemStack) = stack.isIn(ACCEPTED_TAG)
    }
}
