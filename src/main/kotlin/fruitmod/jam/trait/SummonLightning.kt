package fruitmod.jam.trait

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import fruitmod.jam.JamData
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.item.Item
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.hit.HitResult
import java.util.function.Consumer

data class SummonLightning(
    val cosmetic: Boolean = true,
    val targetLookPos: Boolean = false
) : JamActionTrait {
    override val type = TraitTypes.SUMMON_LIGHTNING

    override fun appendTooltip(context: Item.TooltipContext, textConsumer: Consumer<Text>, tooltipType: TooltipType) {
        val text = Text.translatable("trait.fruitmod.summon_lightning.tooltip")
        textConsumer.accept(text)
    }

    override fun performAction(world: ServerWorld, entity: LivingEntity, jam: JamData) {
        val lightning = EntityType.LIGHTNING_BOLT.create(world, SpawnReason.TRIGGERED) ?: return

        val pos = if (targetLookPos) {
            val result = entity.raycast(64.0, 0f, false)
            if (result.type == HitResult.Type.MISS) return

            result.pos
        } else {
            entity.pos
        }

        lightning.refreshPositionAfterTeleport(pos)
        lightning.channeler = entity as? ServerPlayerEntity
        lightning.setCosmetic(cosmetic)

        world.spawnEntity(lightning)
    }

    companion object {
        val CODEC: MapCodec<SummonLightning> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Codec.BOOL.fieldOf("cosmetic").forGetter { it.cosmetic },
                Codec.BOOL.optionalFieldOf("target_look_pos", false).forGetter { it.targetLookPos }
            ).apply(builder, ::SummonLightning)
        }

        val PACKET_CODEC: PacketCodec<in RegistryByteBuf, SummonLightning> =
            PacketCodec.tuple(
                PacketCodecs.BOOLEAN, SummonLightning::cosmetic,
                PacketCodecs.BOOLEAN, SummonLightning::targetLookPos,
                ::SummonLightning
            )
    }
}
