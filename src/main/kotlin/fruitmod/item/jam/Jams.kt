package fruitmod.item.jam

import fruitmod.FruitMod
import fruitmod.ModRegistries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier

object Jams {

    val STRAWBERRY_JAM = registerJam {
        Jam("strawberry", listOf(JamIngredients.STRAWBERRY))
    }

    val KIWI_JAM = registerJam {
        Jam("kiwi", listOf(JamIngredients.KIWI))
    }

    val ORANGE_JAM = registerJam {
        Jam("orange", listOf(JamIngredients.ORANGE))
    }

    val RASPBERRY_JAM = registerJam {
        Jam("raspberry", listOf(JamIngredients.RASPBERRY))
    }

    val STRAWBERRY_KIWI_JAM = registerJam {
        Jam("strawberry_kiwi", listOf(JamIngredients.RASPBERRY, JamIngredients.KIWI))
    }

    val TURBO_JAM = registerJam {
        Jam("turbo", listOf(
                JamIngredients.STRAWBERRY,
                JamIngredients.KIWI,
                JamIngredients.ORANGE,
                JamIngredients.RASPBERRY
            ),
            createNameFromIngredients = false
        )
    }

    fun registerJams(alsoRegisterIngredients: Boolean = true) {
        if (alsoRegisterIngredients) {
            JamIngredients.registerJamIngredients()
        }

        FruitMod.logger.info("Registering Jams for ${FruitMod.MOD_ID}")
    }

    private fun registerJam(
        factory: () -> Jam,
    ): RegistryEntry<Jam> {
        val jam = factory()
        return Registry.registerReference(
            ModRegistries.JAM_REGISTRY,
            Identifier.of(FruitMod.MOD_ID, jam.name),
            jam
        )
    }
}
