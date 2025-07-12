package fruitmod.item.jam

import fruitmod.ModRegistries
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.network.codec.PacketCodecs

data class JamIngredient(
    val name: String,
    val effects: List<StatusEffectInstance>,
    val color: Int = DEFAULT_COLOR
) {
    companion object {
        const val DEFAULT_COLOR = 0xFC5A8D
        const val TRANSLATION_PREFIX = "jam_ingredient"

        val CODEC = ModRegistries.JAM_INGREDIENT_REGISTRY.entryCodec
        val PACKET_CODEC = PacketCodecs.registryEntry(ModRegistries.JAM_INGREDIENT_REGISTRY_KEY)
    }
}
