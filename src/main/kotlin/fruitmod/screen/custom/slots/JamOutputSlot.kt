package fruitmod.screen.custom.slots

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

class JamOutputSlot(
    private val player: PlayerEntity,
    inventory: Inventory,
    index: Int,
    x: Int,
    y: Int
) : Slot(inventory, index, x, y) {

    private var amount = 0

    override fun canInsert(stack: ItemStack) = false

    override fun takeStack(amount: Int): ItemStack {
        if (hasStack()) {
            this.amount += amount.coerceAtMost(stack.count)
        }

        return super.takeStack(amount)
    }

    override fun onTakeItem(player: PlayerEntity, stack: ItemStack) {
        onCrafted(stack)
        super.onTakeItem(player, stack)
    }

    override fun onCrafted(stack: ItemStack, amount: Int) {
        this.amount += amount
        super.onCrafted(stack)
    }

    override fun onCrafted(stack: ItemStack) {
        stack.onCraftByPlayer(player, amount)
        // if (player is ServerPlayerEntity && inventory is JamStationBlockEntity) {
        //    // drop exp for recipes
        // }

        amount = 0;
    }
}