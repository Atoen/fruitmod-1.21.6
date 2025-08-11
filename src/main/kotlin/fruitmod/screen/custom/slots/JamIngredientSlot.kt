package fruitmod.screen.custom.slots

import fruitmod.ModTags
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

class JamIngredientSlot(
    inventory: Inventory,
    index: Int,
    x: Int,
    y: Int,
    private val onChange: () -> Unit
) : Slot(inventory, index, x, y) {

    override fun canInsert(stack: ItemStack) = stack.isIn(ACCEPTED_TAG)

    override fun setStack(stack: ItemStack, previousStack: ItemStack) {
        super.setStack(stack, previousStack)
        if (!ItemStack.areEqual(stack, previousStack)) {
            onChange()
        }
    }

    companion object {
        val ACCEPTED_TAG = ModTags.Items.JAM_INGREDIENT

        fun isValidItem(stack: ItemStack) = stack.isIn(ACCEPTED_TAG)
    }
}
