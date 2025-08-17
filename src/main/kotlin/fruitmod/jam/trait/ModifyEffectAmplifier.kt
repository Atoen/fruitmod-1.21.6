package fruitmod.jam.trait

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import fruitmod.util.withModifiedAmplifier
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Item
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.text.Text
import java.util.function.Consumer

data class ModifyEffectAmplifier(val amplifierChange: Int) : JamModifierTrait {
    override val type = TraitTypes.MODIFY_AMPLIFIER

    override fun appendTooltip(context: Item.TooltipContext, textConsumer: Consumer<Text>, tooltipType: TooltipType) {
        if (amplifierChange == 0) return

        val key = if (amplifierChange > 0) "trait.fruitmod.modify_amplifier.tooltip_add"
        else "trait.fruitmod.modify_amplifier.tooltip_subtract"

        val text = Text.translatable(key, amplifierChange)
        textConsumer.accept(text)
    }

    override fun applyModifier(effects: List<StatusEffectInstance>): List<StatusEffectInstance> {
        return effects.map { it.withModifiedAmplifier(amplifierChange) }
    }

    companion object {
        val CODEC: MapCodec<ModifyEffectAmplifier> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Codec.INT.fieldOf("change").forGetter { it.amplifierChange }
            ).apply(builder, ::ModifyEffectAmplifier)
        }

        val PACKET_CODEC: PacketCodec<in RegistryByteBuf, ModifyEffectAmplifier> =
            PacketCodecs.VAR_INT.xmap(::ModifyEffectAmplifier, ModifyEffectAmplifier::amplifierChange)
    }
}
