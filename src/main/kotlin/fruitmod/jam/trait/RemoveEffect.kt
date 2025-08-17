package fruitmod.jam.trait

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text
import java.util.function.Consumer

data class RemoveEffect(val effect: RegistryEntry<StatusEffect>) : JamModifierTrait {
    override val type = TraitTypes.REMOVE_EFFECT

    override fun appendTooltip(context: Item.TooltipContext, textConsumer: Consumer<Text>, tooltipType: TooltipType) {
        if (!tooltipType.isAdvanced) return

        val effectName = effect.value().name
        val text = Text.translatable("trait.fruitmod.remove_effect.tooltip", effectName)

        textConsumer.accept(text)
    }

    override fun applyModifier(effects: List<StatusEffectInstance>): List<StatusEffectInstance> {
        return effects.filter { it.effectType != effect }
    }

    companion object {
        val CODEC: MapCodec<RemoveEffect> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                StatusEffect.ENTRY_CODEC.fieldOf("effect").forGetter { it.effect }
            ).apply(builder, ::RemoveEffect)
        }

        val PACKET_CODEC: PacketCodec<RegistryByteBuf, RemoveEffect> =
            StatusEffect.ENTRY_PACKET_CODEC.xmap(::RemoveEffect, RemoveEffect::effect)
    }
}
