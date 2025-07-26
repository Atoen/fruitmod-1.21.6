package fruitmod.item.jam

import fruitmod.FruitMod
import fruitmod.ModRegistries
import fruitmod.util.modIdentifier
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry

object JamIngredients {
    val BLUEBERRY = registerIngredient {
        JamIngredient("blueberry", listOf(
            StatusEffectInstance(StatusEffects.NIGHT_VISION, 2400)
        ), 0x4F42B5)
    }

    val CHERRIES = registerIngredient {
        JamIngredient("cherries", listOf(
            StatusEffectInstance(StatusEffects.STRENGTH, 1800)
        ), 0xC21807)
    }

    val COCONUT = registerIngredient {
        JamIngredient("coconut", listOf(
            StatusEffectInstance(StatusEffects.RESISTANCE, 1800)
        ), 0xA17C5B)
    }

    val GRAPES = registerIngredient {
        JamIngredient("grapes", listOf(), 0x6B3FA0)
    }

    val HONEY_BERRIES = registerIngredient {
        JamIngredient("honey_berries", listOf(), 0x5D8AA8)
    }

    val KIWI = registerIngredient {
        JamIngredient("kiwi", listOf(
            StatusEffectInstance(StatusEffects.JUMP_BOOST, 1800)
        ), 0x8DB600)
    }

    val LEMON = registerIngredient {
        JamIngredient("lemon", listOf(), 0xFFF700)
    }

    val LEMON_SLICE = registerIngredient {
        JamIngredient("lemon_slice", listOf(), 0xFFF86C)
    }

    val LIME = registerIngredient {
        JamIngredient("lime", listOf(), 0xBFFF00)
    }

    val LIME_SLICE = registerIngredient {
        JamIngredient("lime_slice", listOf(), 0xD4FF66)
    }

    val MANGO = registerIngredient {
        JamIngredient("mango", listOf(
            StatusEffectInstance(StatusEffects.HASTE, 1800)
        ), 0xFFA62B)
    }

    val ORANGE = registerIngredient {
        JamIngredient("orange", listOf(
            StatusEffectInstance(StatusEffects.SPEED, 3600)
        ), 0xFFA500)
    }

    val PAPAYA = registerIngredient {
        JamIngredient("papaya", listOf(), 0xFF7518)
    }

    val PEACH = registerIngredient {
        JamIngredient("peach", listOf(
            StatusEffectInstance(StatusEffects.REGENERATION, 900)
        ), 0xFFDAB9)
    }

    val PEAR = registerIngredient {
        JamIngredient("pear", listOf(), 0xD1E231)
    }

    val PINEAPPLE = registerIngredient {
        JamIngredient("pineapple", listOf(), 0xFCD116)
    }

    val RASPBERRY = registerIngredient {
        JamIngredient("raspberry", listOf(
            StatusEffectInstance(StatusEffects.STRENGTH, 3600)
        ), 0xE30B5C)
    }

    val STRAWBERRY = registerIngredient {
        JamIngredient("strawberry", listOf(
            StatusEffectInstance(StatusEffects.ABSORPTION, 3600, 2)
        ), 0xFC5A8D)
    }

    fun registerJamIngredients() {
        FruitMod.logger.info("Registering Jam Ingredients for ${FruitMod.MOD_ID}")
    }

    private fun registerIngredient(
        factory: () -> JamIngredient
    ): RegistryEntry<JamIngredient> {
        val ingredient = factory()

        return Registry.registerReference(
            ModRegistries.JAM_INGREDIENT_REGISTRY,
            modIdentifier(ingredient.name),
            ingredient
        )
    }
}