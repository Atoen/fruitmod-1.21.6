package fruitmod.item.jam

import fruitmod.ModRegistries
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.entry.RegistryEntry

class Jam(val name: String, val ingredients: List<RegistryEntry<JamIngredient>>) {
    companion object {
        val CODEC = ModRegistries.JAM_REGISTRY.entryCodec
        val PACKET_CODEC = PacketCodecs.registryEntry(ModRegistries.JAM_REGISTRY_KEY)
    }
}
