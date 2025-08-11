package fruitmod.item.jam

import com.mojang.serialization.Codec
import fruitmod.ModRegistries
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.entry.RegistryEntry

class Jam(
    val name: String,
    val ingredients: List<RegistryEntry<JamIngredient>>,
    val createNameFromIngredients : Boolean = true
) {
    companion object {
        val CODEC: Codec<RegistryEntry<Jam>> = ModRegistries.JAM.entryCodec
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, RegistryEntry<Jam>> =
            PacketCodecs.registryEntry(ModRegistries.JAM_REGISTRY_KEY)
    }
}
