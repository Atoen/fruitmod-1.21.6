package fruitmod.item.custom

import fruitmod.component.JamComponent
import fruitmod.component.JamConsumableComponent
import fruitmod.component.ModDataComponents
import fruitmod.item.jam.Jams
import net.minecraft.component.type.TooltipDisplayComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.consume.UseAction
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.StringIdentifiable
import net.minecraft.world.World
import java.util.function.Consumer

class JamItem(settings: Settings): Item(settings) {

    override fun getDefaultStack(): ItemStack {
        return defaultStack.apply {
            set(ModDataComponents.JAMS, JamComponent(Jams.STRAWBERRY_JAM))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        displayComponent: TooltipDisplayComponent,
        textConsumer: Consumer<Text>,
        type: TooltipType
    ) {
        stack.get(ModDataComponents.JAMS)?.run {
            appendTooltip(context, textConsumer, type, stack.components)
        }
    }

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        val jamConsumableComponent = stack.get(ModDataComponents.JAM_CONSUMABLE)
        jamConsumableComponent?.let {
            if (it.shouldPlaySound(remainingUseTicks)) {
                it.playSound(user)
            }
        }
    }

    override fun getUseAction(stack: ItemStack): UseAction {
        return UseAction.DRINK
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
        val stack = user.getStackInHand(hand)
        val jamConsumableComponent = stack.get(ModDataComponents.JAM_CONSUMABLE)

        if (jamConsumableComponent != null) {
            return jamConsumableComponent.consume(user, stack, hand)
        }

        return ActionResult.PASS
    }

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        val jamConsumableComponent = stack.get(ModDataComponents.JAM_CONSUMABLE)
        return jamConsumableComponent?.finishConsumption(world, user, stack) ?: stack
    }

    override fun getMaxUseTime(stack: ItemStack, user: LivingEntity): Int {
        val jamConsumableComponent = stack.get(ModDataComponents.JAM_CONSUMABLE)
        if (jamConsumableComponent == null) {
            return JamConsumableComponent.DEFAULT_CONSUME_TICKS
        }

        return jamConsumableComponent.consumeTicks
    }

    override fun getName(stack: ItemStack): Text {
        val jam = stack.get(ModDataComponents.JAMS)
        return jam?.name ?: super.getName(stack)
    }

    companion object {
        fun getRemainingPortions(stack: ItemStack): Int {
            val jamComponent = stack.get(ModDataComponents.JAMS)
            return jamComponent?.portions ?: 0
        }
    }

    enum class Portions(val value: String): StringIdentifiable {
        NONE("none"),
        ONE("one"),
        TWO("two"),
        THREE("three"),
        FOUR_OR_MORE("four_or_more");

        override fun asString() = value

        companion object {
            val CODEC: StringIdentifiable.EnumCodec<Portions> =
                StringIdentifiable.createCodec(Portions::values)
        }
    }
}
