package fruitmod.util

import fruitmod.FruitMod
import net.minecraft.component.type.PotionContentsComponent
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectUtil
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text
import net.minecraft.util.Identifier

fun modIdentifier(name: String): Identifier = Identifier.of(FruitMod.MOD_ID, name)

fun Item.toRegistryEntry(): RegistryEntry<Item> {
    return Registries.ITEM.getEntry(this)
}

fun StatusEffectInstance.withModifiedAmplifier(
    amplifierChange: Int
): StatusEffectInstance {
    return StatusEffectInstance(
        effectType,
        duration,
        amplifier + amplifierChange,
        isAmbient,
        shouldShowParticles(),
        shouldShowIcon()
    )
}

fun StatusEffectInstance.modifyDurationAndAmplifier(
    durationScale: Float,
    amplifierChange: Int
): StatusEffectInstance {
    return StatusEffectInstance(
        effectType,
        (duration * durationScale).toInt(),
        amplifier + amplifierChange,
        isAmbient,
        shouldShowParticles(),
        shouldShowIcon()
    )
}

fun StatusEffectInstance.getFullTextFormatted(updateTickRate: Float, durationMultiplier: Float = 1f): Text {
    var effectText = PotionContentsComponent.getEffectText(effectType, amplifier)
    if (isDurationBelow(20)) {
        val durationText = StatusEffectUtil.getDurationText(this, durationMultiplier, updateTickRate)
        effectText = Text.translatable("potion.withDuration", effectText, durationText)
    }

    val formatting = effectType.value().category.formatting
    val formatted = effectText.formatted(formatting)

    return formatted
}
