package fruitmod.screen.custom.slots

import fruitmod.item.ModItems
import fruitmod.util.modIdentifier
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.util.Identifier

class JamJarSlot(
    inventory: Inventory,
    index: Int,
    x: Int,
    y: Int
) : Slot(inventory, index, x, y) {

    override fun canInsert(stack: ItemStack) = stack.isOf(ACCEPTED_ITEM)

    override fun getBackgroundSprite(): Identifier {
        return SPRITE
    }

    companion object {
        private val SPRITE = modIdentifier("container/slot/jar")

        val ACCEPTED_ITEM = ModItems.JAR

        fun isValidItem(stack: ItemStack) = stack.isOf(ACCEPTED_ITEM)
    }
}
