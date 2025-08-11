package fruitmod.item.jam

import fruitmod.FruitMod
import fruitmod.ModRegistries
import fruitmod.util.modIdentifier
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry

object Jams {

//    val STRAWBERRY_JAM = registerJam {
//        Jam("strawberry", listOf(JamIngredients.STRAWBERRY))
//    }
//
//    val KIWI_JAM = registerJam {
//        Jam("kiwi", listOf(JamIngredients.KIWI))
//    }
//
//    val ORANGE_JAM = registerJam {
//        Jam("orange", listOf(JamIngredients.ORANGE))
//    }
//
//    val RASPBERRY_JAM = registerJam {
//        Jam("raspberry", listOf(JamIngredients.RASPBERRY))
//    }
//
//    val STRAWBERRY_KIWI_JAM = registerJam {
//        Jam("strawberry_kiwi", listOf(JamIngredients.RASPBERRY, JamIngredients.KIWI))
//    }

//    val

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

    val GOLDEN_JAM = registerJam {
        Jam("golden", listOf(
                JamIngredients.PEAR,
                JamIngredients.ENCHANTED_GOLDEN_APPLE
            ),
            createNameFromIngredients = false
        )
    }

    fun registerJams(alsoRegisterIngredients: Boolean = true) {
        if (alsoRegisterIngredients) {
            JamIngredients.registerJamIngredients()
        }

        FruitMod.logger.info("Registering Jams for {}", FruitMod.MOD_ID)
    }

    private fun registerJam(
        factory: () -> Jam,
    ): RegistryEntry<Jam> {
        val jam = factory()
        return Registry.registerReference(
            ModRegistries.JAM,
            modIdentifier(jam.name),
            jam
        )
    }
}
