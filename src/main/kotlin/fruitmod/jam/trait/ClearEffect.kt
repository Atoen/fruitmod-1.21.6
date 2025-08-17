package fruitmod.jam.trait

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import fruitmod.jam.JamData
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import java.util.function.Consumer

data class ClearEffect(val effect: RegistryEntry<StatusEffect>) : JamActionTrait {
    override val type = TraitTypes.CLEAR_EFFECT
    override val deduplicationKey = this

    override fun appendTooltip(context: Item.TooltipContext, textConsumer: Consumer<Text>, tooltipType: TooltipType) {
        val effectName = effect.value().name
        val text = Text.translatable("trait.fruitmod.clear_effect.tooltip", effectName)

        textConsumer.accept(text)
    }

    override fun performAction(world: ServerWorld, entity: LivingEntity, jam: JamData) {
        entity.removeStatusEffect(effect)
    }

    companion object {
        val CODEC: MapCodec<ClearEffect> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                StatusEffect.ENTRY_CODEC.fieldOf("effect").forGetter { it.effect }
            ).apply(builder, ::ClearEffect)
        }

        val PACKET_CODEC: PacketCodec<RegistryByteBuf, ClearEffect> =
            StatusEffect.ENTRY_PACKET_CODEC.xmap(::ClearEffect, ClearEffect::effect)
    }
}
