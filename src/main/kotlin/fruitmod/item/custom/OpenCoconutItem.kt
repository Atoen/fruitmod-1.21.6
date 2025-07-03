package fruitmod.item.custom

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.consume.UseAction
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

class OpenCoconutItem(
    settings: Settings
) : Item(settings) {

    override fun getUseAction(stack: ItemStack): UseAction {
        return UseAction.DRINK
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
        user.setCurrentHand(hand)
        return ActionResult.CONSUME
    }

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        if (user is PlayerEntity && !user.abilities.creativeMode) {
            stack.decrement(1)
        }

        return stack
    }

    override fun getMaxUseTime(stack: ItemStack?, user: LivingEntity?): Int {
        return super.getMaxUseTime(stack, user)
    }
}