package fruitmod.jam.trait

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import fruitmod.jam.JamData
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.function.Consumer

data class SetOnFire(val seconds: Float = 10f) : JamActionTrait {
    override val type = TraitTypes.SET_ON_FIRE

    override fun appendTooltip(context: Item.TooltipContext, textConsumer: Consumer<Text>, tooltipType: TooltipType) {
        val text = Text.translatable("trait.fruitmod.set_on_fire.tooltip").formatted(Formatting.YELLOW)
        textConsumer.accept(text)
    }

    override fun performAction(world: ServerWorld, entity: LivingEntity, jam: JamData) {
        entity.setOnFireFor(seconds)
    }

    companion object {
        val CODEC: MapCodec<SetOnFire> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Codec.FLOAT.fieldOf("seconds").forGetter { it.seconds }
            ).apply(builder, ::SetOnFire)
        }

        val PACKET_CODEC: PacketCodec<in RegistryByteBuf, SetOnFire> =
            PacketCodecs.FLOAT.xmap(::SetOnFire, SetOnFire::seconds)
    }
}
