package fruitmod.jam.base

import com.mojang.serialization.Codec
import fruitmod.ModRegistries
import fruitmod.jam.trait.JamTrait
import net.minecraft.item.Item
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text
import net.minecraft.util.math.ColorHelper
import java.util.*

data class JamBase(
    val item: RegistryEntry<Item>,
    val traits: List<JamTrait> = emptyList(),
    val customNameKey: Optional<String> = Optional.empty(),
    val color: OptionalInt = OptionalInt.empty(),
    val colorInfluence: Float = 0.2f
) {
    val name: Text get() {
        return if (customNameKey.isPresent) {
            Text.translatable(customNameKey.get())
        } else {
            item.value().name
        }
    }

    fun applyColor(color: Int): Int {
        if (this.color.isEmpty) {
            return color
        }

        return ColorHelper.lerp(colorInfluence, color, this.color.asInt)
    }

    companion object {
        val CODEC: Codec<RegistryEntry<JamBase>> = ModRegistries.JAM_BASE.entryCodec
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, RegistryEntry<JamBase>> =
            PacketCodecs.registryEntry(ModRegistries.JAM_BASE_REGISTRY_KEY)
    }
}
