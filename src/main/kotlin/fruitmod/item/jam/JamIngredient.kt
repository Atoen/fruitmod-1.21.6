package fruitmod.item.jam

import com.mojang.serialization.Codec
import fruitmod.FruitMod
import fruitmod.ModRegistries
import fruitmod.util.JamIngredientCategory
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text

data class JamIngredient(
    val name: String,
    val effects: List<StatusEffectInstance>,
    val color: Int = DEFAULT_COLOR,
    val category: JamIngredientCategory = JamIngredientCategory.REGULAR
) {
    val translationKey = "jam_ingredient.${FruitMod.MOD_ID}.$name"
    val translatableText: Text
        get() = Text.translatable(translationKey).formatted(category.format)

    companion object {
        const val DEFAULT_COLOR = 0xFC5A8D

        val CODEC: Codec<RegistryEntry<JamIngredient>> =
            ModRegistries.JAM_INGREDIENT_REGISTRY.entryCodec

        val PACKET_CODEC: PacketCodec<RegistryByteBuf, RegistryEntry<JamIngredient>> =
            PacketCodecs.registryEntry(ModRegistries.JAM_INGREDIENT_REGISTRY_KEY)
    }
}
