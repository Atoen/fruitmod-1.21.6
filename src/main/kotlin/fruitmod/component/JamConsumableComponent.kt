package fruitmod.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import fruitmod.item.ModItems
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.dynamic.Codecs
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent

data class JamConsumableComponent(val consumeSeconds: Float) {

    fun consume(user: LivingEntity, stack: ItemStack, hand: Hand): ActionResult {
        val hasConsumeTime = consumeTicks > 0
        if (hasConsumeTime) {
            user.setCurrentHand(hand)
            return ActionResult.CONSUME
        } else {
            val stack = finishConsumption(user.world, user, stack)
            return ActionResult.CONSUME.withNewHandStack(stack)
        }
    }

    val consumeTicks: Int
        get() = (20 * consumeSeconds).toInt()

    fun finishConsumption(world: World, user: LivingEntity, stack: ItemStack): ItemStack {
        playSound(user)

        stack.run {
            get(ModDataComponents.JAM)?.onConsume(world, user)
            get(DataComponentTypes.FOOD)?.let {
                onFinishedConsumption(world, user, it)
            }
        }

        user.emitGameEvent(GameEvent.DRINK)

        return decrementPortionsUnlessCreative(stack, user)
    }

    fun decrementPortionsUnlessCreative(stack: ItemStack, user: LivingEntity): ItemStack {
        if (user.isInCreativeMode) return stack

        val jamComponent = stack.get(ModDataComponents.JAM) ?: return stack
        val remainingPortions = (jamComponent.portions - 1).coerceAtLeast(0)

        if (remainingPortions == 0) {
            user.setStackInHand(user.activeHand, ModItems.JAR.defaultStack)
        } else {
            val updated = jamComponent.copy(portions = remainingPortions)
            stack.set(ModDataComponents.JAM, updated)
        }

        return stack
    }

    fun shouldPlaySound(remainingUseTicks: Int): Boolean {
        val elapsedTicks = consumeTicks - remainingUseTicks
        val triggerThreshold = (consumeTicks * SOUND_TRIGGER_FRACTION).toInt()
        val isPastThreshold = elapsedTicks > triggerThreshold
        val isOnInterval = remainingUseTicks % 4 == 0

        return isPastThreshold && isOnInterval
    }

    fun playSound(user: LivingEntity) {
        val sound = SoundEvents.ITEM_HONEY_BOTTLE_DRINK.value()
        user.playSound(sound, 0.5f, 0.95f)
    }

    private fun onFinishedConsumption(
        world: World,
        user: LivingEntity,
        foodComponent: FoodComponent
    ) {
        if (user is PlayerEntity) {
            user.hungerManager.eat(foodComponent)
            world.playSound(
                null,
                user.x, user.y, user.z,
                SoundEvents.ENTITY_PLAYER_BURP,
                SoundCategory.PLAYERS,
                0.5f,
                MathHelper.nextBetween(user.random, 0.9f, 1f)
            )
        }
    }

    companion object {

        private const val SOUND_TRIGGER_FRACTION = 0.2f

        const val DEFAULT_CONSUME_SECONDS: Float = 1.6f
        const val DEFAULT_CONSUME_TICKS: Int = (20 * DEFAULT_CONSUME_SECONDS).toInt()

        val DEFAULT = JamConsumableComponent(DEFAULT_CONSUME_SECONDS)

        val CODEC: Codec<JamConsumableComponent> = RecordCodecBuilder.create { builder ->
            builder.group(
                Codecs.NON_NEGATIVE_FLOAT.fieldOf("consume_seconds").forGetter { it.consumeSeconds }
            ).apply(builder, ::JamConsumableComponent)
        }

        val PACKET_CODEC: PacketCodec<RegistryByteBuf, JamConsumableComponent> = PacketCodec.tuple(
            PacketCodecs.FLOAT,
            JamConsumableComponent::consumeSeconds,
            ::JamConsumableComponent
        )
    }
}
