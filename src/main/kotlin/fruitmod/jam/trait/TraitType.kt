package fruitmod.jam.trait

import com.mojang.serialization.MapCodec
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec

data class TraitType<T : JamTrait>(
    val codec: MapCodec<T>,
    val packetCodec: PacketCodec<in RegistryByteBuf, T>
)
