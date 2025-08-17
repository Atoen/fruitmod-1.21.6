package fruitmod.jam.trait

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Item
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.text.Text
import java.util.function.Consumer

data class AddEffect(val effect: StatusEffectInstance) : JamModifierTrait {
    override val type = TraitTypes.ADD_EFFECT

    override fun appendTooltip(context: Item.TooltipContext, textConsumer: Consumer<Text>, tooltipType: TooltipType) {
        if (!tooltipType.isAdvanced) return

        val effectName = effect.effectType.value().name
        val text = Text.translatable("trait.fruitmod.add_effect.tooltip", effectName)

        textConsumer.accept(text)
    }

    override fun applyModifier(effects: List<StatusEffectInstance>): List<StatusEffectInstance> {
        return effects + effect
    }

    companion object {
        val CODEC: MapCodec<AddEffect> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                StatusEffectInstance.CODEC.fieldOf("effect").forGetter { it.effect }
            ).apply(builder, ::AddEffect)
        }

        val PACKET_CODEC: PacketCodec<RegistryByteBuf, AddEffect> =
            StatusEffectInstance.PACKET_CODEC.xmap(::AddEffect, AddEffect::effect)
    }
}
