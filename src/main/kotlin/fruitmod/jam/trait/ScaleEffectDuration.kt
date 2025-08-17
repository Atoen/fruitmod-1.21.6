package fruitmod.jam.trait

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Item
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.text.Text
import java.util.function.Consumer

data class ScaleEffectDuration(val scale: Float) : JamModifierTrait {
    override val type = TraitTypes.MODIFY_DURATION

    override fun appendTooltip(context: Item.TooltipContext, textConsumer: Consumer<Text>, tooltipType: TooltipType) {
        if (scale == 1f) return

        val text = Text.translatable("trait.fruitmod.scale_duration.tooltip", scale)
        textConsumer.accept(text)
    }

    override fun applyModifier(effects: List<StatusEffectInstance>): List<StatusEffectInstance> {
        return effects.map { it.withScaledDuration(scale) }
    }

    companion object {
        val CODEC: MapCodec<ScaleEffectDuration> = RecordCodecBuilder.mapCodec{ builder ->
            builder.group(
                Codec.FLOAT.fieldOf("scale").forGetter { it.scale }
            ).apply(builder, ::ScaleEffectDuration)
        }

        val PACKET_CODEC: PacketCodec<in RegistryByteBuf, ScaleEffectDuration> =
            PacketCodecs.FLOAT.xmap(::ScaleEffectDuration, ScaleEffectDuration::scale)
    }
}
