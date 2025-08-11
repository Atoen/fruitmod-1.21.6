package fruitmod.item.jam

import com.mojang.serialization.Codec
import fruitmod.ModRegistries
import net.minecraft.item.Item
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.entry.RegistryEntry

data class JamBase(
    val name: String,
    val item: RegistryEntry<Item>
) {
    companion object {
        val CODEC: Codec<RegistryEntry<JamBase>> = ModRegistries.JAM_BASE.entryCodec
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, RegistryEntry<JamBase>> =
            PacketCodecs.registryEntry(ModRegistries.JAM_BASE_REGISTRY_KEY)
    }
}