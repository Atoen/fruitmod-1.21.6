package fruitmod.jam.trait

import com.mojang.serialization.Codec
import fruitmod.FruitMod
import fruitmod.jam.JamData
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Item
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import java.util.function.Consumer

interface JamTrait {
    val type: TraitType<*>
    val deduplicationKey: Any

    fun appendTooltip(context: Item.TooltipContext, textConsumer: Consumer<Text>, tooltipType: TooltipType)

    companion object {
        fun registerJamTraits() {
            FruitMod.logger.info("Registering Jam Traits for {}", FruitMod.MOD_ID)
        }

        val CODEC: Codec<JamTrait> =
            TraitTypes.CODEC.dispatch("type", JamTrait::type, TraitType<*>::codec)

        val PACKET_CODEC: PacketCodec<RegistryByteBuf, JamTrait> =
            TraitTypes.PACKET_CODEC.dispatch(JamTrait::type) {
                it.packetCodec as PacketCodec<RegistryByteBuf, JamTrait>
            }
    }
}

interface JamModifierTrait : JamTrait {
    override val deduplicationKey get() = this

    fun applyModifier(effects: List<StatusEffectInstance>): List<StatusEffectInstance>
}

interface JamActionTrait : JamTrait {
    override val deduplicationKey: Any get() = this::class

    fun performAction(world: ServerWorld, entity: LivingEntity, jam: JamData)
}
