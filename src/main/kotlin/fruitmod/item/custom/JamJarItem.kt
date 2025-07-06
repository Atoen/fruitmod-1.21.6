package fruitmod.item.custom

import fruitmod.ModComponents
import fruitmod.ModRegistries
import fruitmod.item.jam.Jam
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.consume.UseAction
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World

class JamJarItem(
    settings: Settings,
    val color: Int
) : Item(settings) {

    override fun getUseAction(stack: ItemStack) = UseAction.DRINK

    override fun getMaxUseTime(stack: ItemStack, user: LivingEntity) = 32

    override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
        user.setCurrentHand(hand)
        return ActionResult.CONSUME
    }

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        if (!world.isClient && user is PlayerEntity) {
            val ingredientIds = stack.get(ModComponents.JAM_INGREDIENTS) ?: return stack
            val ingredients = ingredientIds.mapNotNull {
                ModRegistries.JAM_INGREDIENT_REGISTRY.get(it)
            }

            val jam = Jam("c", ingredients)
            jam.combinedEffects.forEach {
                user.addStatusEffect(it)
            }

            stack.decrementUnlessCreative(1, user)
        }

        return stack
    }

    companion object {
        fun createWithIngredients(base: JamJarItem, vararg ingredientIds: Identifier): ItemStack {
            val stack = ItemStack(base)
            stack.set(ModComponents.JAM_INGREDIENTS, ingredientIds.toList())
            return stack
        }
    }
}