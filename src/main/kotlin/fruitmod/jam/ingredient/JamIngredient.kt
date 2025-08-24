package fruitmod.jam.ingredient

import com.mojang.serialization.Codec
import fruitmod.ModRegistries
import fruitmod.jam.trait.JamTrait
import fruitmod.util.JamIngredientCategory
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Item
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text
import java.util.*

data class JamIngredient(
    val item: RegistryEntry<Item>,
    val effects: List<StatusEffectInstance>,
    val color: Int = DEFAULT_COLOR,
    val customNameKey: Optional<String> = Optional.empty(),
    val trait: Optional<JamTrait> = Optional.empty(),
    val category: JamIngredientCategory = JamIngredientCategory.REGULAR,
) {
    val name: Text get() {
        return if (customNameKey.isPresent) {
            Text.translatable(customNameKey.get())
        } else {
            item.value().name
        }
    }

    companion object {
        const val DEFAULT_COLOR = 0xFC5A8D

        val CODEC: Codec<RegistryEntry<JamIngredient>> =
            ModRegistries.JAM_INGREDIENT.entryCodec

        val PACKET_CODEC: PacketCodec<RegistryByteBuf, RegistryEntry<JamIngredient>> =
            PacketCodecs.registryEntry(ModRegistries.JAM_INGREDIENT_REGISTRY_KEY)
    }
}
