package fruitmod.item.food

import net.minecraft.component.type.ConsumableComponent
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.consume.ApplyEffectsConsumeEffect
import net.minecraft.item.consume.UseAction
import net.minecraft.sound.SoundEvents

object ModConsumableComponents {

    val FAST_FRUIT: ConsumableComponent = food().consumeSeconds(0.8f).build()

    val BASIC_FRUIT: ConsumableComponent = food().consumeSeconds(1.6f).build()

    val JUICY_FRUIT: ConsumableComponent = food()
        .consumeSeconds(1.2f)
        .consumeEffect(ApplyEffectsConsumeEffect(StatusEffectInstance(StatusEffects.SATURATION, 20, 0), 0.15f))
        .build()

    val LOW_NUTRITION_FRUIT: ConsumableComponent = food()
        .consumeSeconds(0.8f)
        .build()

    val SLICE: ConsumableComponent = food()
        .consumeSeconds(0.7f)
        .consumeEffect(ApplyEffectsConsumeEffect(StatusEffectInstance(StatusEffects.SATURATION, 10, 0), 0.1f))
        .build()

    val SOUR_SLICE: ConsumableComponent = food()
        .consumeSeconds(0.7f)
        .consumeEffect(ApplyEffectsConsumeEffect(StatusEffectInstance(StatusEffects.HASTE, 60, 0), 0.3f))
        .build()

    val HYDRATING_FRUIT: ConsumableComponent = drink()
        .consumeSeconds(1.0f)
        .consumeEffect(ApplyEffectsConsumeEffect(StatusEffectInstance(StatusEffects.REGENERATION, 60, 0)))
        .build()

    val BANANA: ConsumableComponent = food()
        .consumeSeconds(3f)
        .consumeEffect(ApplyEffectsConsumeEffect(StatusEffectInstance(StatusEffects.SPEED, 100, 0), 0.5f))
        .build()

    val PINEAPPLE: ConsumableComponent = food()
        .consumeSeconds(5f)
        .build()

    private fun food(): ConsumableComponent.Builder {
        return ConsumableComponent.builder()
            .consumeSeconds(1.6f)
            .useAction(UseAction.EAT)
            .sound(SoundEvents.ENTITY_GENERIC_EAT)
            .consumeParticles(true)
    }

    private fun drink(): ConsumableComponent.Builder {
        return ConsumableComponent.builder()
            .consumeSeconds(1.6f)
            .useAction(UseAction.DRINK)
            .sound(SoundEvents.ENTITY_GENERIC_DRINK)
            .consumeParticles(false)
    }
}