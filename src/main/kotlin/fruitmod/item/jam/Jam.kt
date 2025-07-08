package fruitmod.item.jam

import fruitmod.FruitMod
import fruitmod.ModRegistries
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier

class Jam(val name: String, vararg ingredient: JamIngredient) {
    companion object {
        val CODEC = ModRegistries.JAM_REGISTRY.entryCodec
        val PACKET_CODEC = PacketCodecs.registryEntry(ModRegistries.JAM_REGISTRY_KEY)
    }
}

object Jams {

    val STRAWBERRY_JAM = registerJam("strawberry", Jam("strawberry", ModJamIngredients.STRAWBERRY.value()))

    fun registerJams() {
        FruitMod.logger.info("Registering Jams for ${FruitMod.MOD_ID}")
    }

    private fun registerJam(name: String, jam: Jam): RegistryEntry<Jam> {
        return Registry.registerReference(ModRegistries.JAM_REGISTRY, Identifier.of(FruitMod.MOD_ID, name), jam)
    }
}

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