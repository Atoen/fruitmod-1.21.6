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
import net.minecraft.world.World
import java.util.function.Consumer

data class Explode(
    val power: Float = 1f,
    val createFire: Boolean = false,
    val destroyBlocks: Boolean = false
) : JamActionTrait {
    override val type = TraitTypes.EXPLODE

    override fun appendTooltip(context: Item.TooltipContext, textConsumer: Consumer<Text>, tooltipType: TooltipType) {
        val text = Text.translatable("trait.fruitmod.explode.tooltip").formatted(Formatting.YELLOW)
        textConsumer.accept(text)
    }

    override fun performAction(world: ServerWorld, entity: LivingEntity, jam: JamData) {
        val explosionSourceType = if (destroyBlocks) World.ExplosionSourceType.TNT else World.ExplosionSourceType.NONE

        world.createExplosion(
           entity,
            entity.x, entity.y, entity.z,
            power,
            createFire,
            explosionSourceType
        )
    }

    companion object {
        val CODEC: MapCodec<Explode> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Codec.FLOAT.fieldOf("power").forGetter { it.power },
                Codec.BOOL.fieldOf("create_fire").forGetter { it.createFire },
                Codec.BOOL.fieldOf("destroy_blocks").forGetter { it.destroyBlocks }
            ).apply(builder, ::Explode)
        }

        val PACKET_CODEC: PacketCodec<RegistryByteBuf, Explode> =
            PacketCodec.tuple(
                PacketCodecs.FLOAT, Explode::power,
                PacketCodecs.BOOLEAN, Explode::createFire,
                PacketCodecs.BOOLEAN, Explode::destroyBlocks,
                ::Explode
            )
    }
}
