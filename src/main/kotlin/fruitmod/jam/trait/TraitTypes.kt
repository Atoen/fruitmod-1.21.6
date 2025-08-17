package fruitmod.jam.trait

import com.mojang.serialization.Codec
import fruitmod.FruitMod
import fruitmod.ModRegistries
import fruitmod.util.modIdentifier
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registry

object TraitTypes {

    val ADD_EFFECT = register(
        "add_effect",
        TraitType(AddEffect.CODEC, AddEffect.PACKET_CODEC)
    )

    val REMOVE_EFFECT = register(
        "remove_effect",
        TraitType(RemoveEffect.CODEC, RemoveEffect.PACKET_CODEC)
    )

    val CLEAR_EFFECT = register(
        "clear_effect",
        TraitType(ClearEffect.CODEC, ClearEffect.PACKET_CODEC)
    )

    val MODIFY_DURATION = register(
        "duration",
        TraitType(ScaleEffectDuration.CODEC, ScaleEffectDuration.PACKET_CODEC)
    )

    val MODIFY_AMPLIFIER = register(
        "amplifier",
        TraitType(ModifyEffectAmplifier.CODEC, ModifyEffectAmplifier.PACKET_CODEC)
    )

    val SET_ON_FIRE = register(
        "set_on_fire",
        TraitType(SetOnFire.CODEC, SetOnFire.PACKET_CODEC)
    )

    val EXPLODE = register(
        "explode",
        TraitType(Explode.CODEC, Explode.PACKET_CODEC)
    )

    val RANDOM_TELEPORT = register(
        "random_teleport",
        TraitType(RandomTeleport.CODEC, RandomTeleport.PACKET_CODEC)
    )

    fun registerTraitTypes() {
        FruitMod.logger.info("Registering Jam Traits Types for {}", FruitMod.MOD_ID)
    }

    private fun <T : JamTrait> register(id: String, type: TraitType<T>): TraitType<T> {
        return Registry.register(ModRegistries.JAM_TRAIT, modIdentifier(id), type)
    }

    val CODEC: Codec<TraitType<*>> = ModRegistries.JAM_TRAIT.codec
    val PACKET_CODEC: PacketCodec<RegistryByteBuf, TraitType<*>> = PacketCodecs.registryCodec(CODEC)
}
