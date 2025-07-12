package fruitmod.item.jam

import fruitmod.FruitMod
import fruitmod.ModRegistries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier

object Jams {

    val STRAWBERRY_JAM = registerJam("strawberry") {
        Jam("strawberry", listOf(JamIngredients.STRAWBERRY))
    }

    val KIWI_JAM = registerJam("kiwi") {
        Jam("kiwi", listOf(JamIngredients.KIWI))
    }

    val ORANGE_JAM = registerJam("orange") {
        Jam("orange", listOf(JamIngredients.ORANGE))
    }

    val RASPBERRY_JAM = registerJam("raspberry") {
        Jam("raspberry", listOf(JamIngredients.RASPBERRY))
    }

    val TURBO_JAM = registerJam("turbo") {
        Jam("turbo", listOf(
            JamIngredients.STRAWBERRY,
            JamIngredients.KIWI,
            JamIngredients.ORANGE,
            JamIngredients.RASPBERRY
        ))
    }

    fun registerJams(alsoRegisterIngredients: Boolean = true) {
        if (alsoRegisterIngredients) {
            JamIngredients.registerJamIngredients()
        }

        FruitMod.logger.info("Registering Jams for ${FruitMod.MOD_ID}")
    }

    private fun registerJam(
        name: String,
        factory: () -> Jam,
    ): RegistryEntry<Jam> {
        val jam = factory()
        return Registry.registerReference(
            ModRegistries.JAM_REGISTRY,
            Identifier.of(FruitMod.MOD_ID, name),
            jam
        )
    }
}
