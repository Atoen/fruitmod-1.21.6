package fruitmod.item.jam

import fruitmod.FruitMod
import fruitmod.ModRegistries
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier

object ModJamIngredients {
    val BLUEBERRY = registerIngredient("blueberry") {
        JamIngredient("blueberry", listOf(
            StatusEffectInstance(StatusEffects.NIGHT_VISION, 2400)
        ), 0x4F42B5)
    }

    val CHERRIES = registerIngredient("cherries") {
        JamIngredient("cherries", listOf(
            StatusEffectInstance(StatusEffects.STRENGTH, 1800)
        ), 0xC21807)
    }

    val COCONUT = registerIngredient("coconut") {
        JamIngredient("coconut", listOf(
            StatusEffectInstance(StatusEffects.RESISTANCE, 1800)
        ), 0xA17C5B)
    }

    val GRAPES = registerIngredient("grapes") {
        JamIngredient("grapes", listOf(), 0x6B3FA0)
    }

    val HONEY_BERRIES = registerIngredient("honey_berries") {
        JamIngredient("honey_berries", listOf(), 0x5D8AA8)
    }

    val KIWI = registerIngredient("kiwi") {
        JamIngredient("kiwi", listOf(
            StatusEffectInstance(StatusEffects.JUMP_BOOST, 1800)
        ), 0x8DB600)
    }

    val LEMON = registerIngredient("lemon") {
        JamIngredient("lemon", listOf(), 0xFFF700)
    }

    val LEMON_SLICE = registerIngredient("lemon_slice") {
        JamIngredient("lemon_slice", listOf(), 0xFFF86C)
    }

    val LIME = registerIngredient("lime") {
        JamIngredient("lime", listOf(), 0xBFFF00)
    }

    val LIME_SLICE = registerIngredient("lime_slice") {
        JamIngredient("lime_slice", listOf(), 0xD4FF66)
    }

    val MANGO = registerIngredient("mango") {
        JamIngredient("mango", listOf(
            StatusEffectInstance(StatusEffects.HASTE, 1800)
        ), 0xFFA62B)
    }

    val ORANGE = registerIngredient("orange") {
        JamIngredient("orange", listOf(), 0xFFA500)
    }

    val ORANGE_SLICE = registerIngredient("orange_slice") {
        JamIngredient("orange_slice", listOf(), 0xFFB347)
    }

    val PAPAYA = registerIngredient("papaya") {
        JamIngredient("papaya", listOf(), 0xFF7518)
    }

    val PEACH = registerIngredient("peach") {
        JamIngredient("peach", listOf(
            StatusEffectInstance(StatusEffects.REGENERATION, 900)
        ), 0xFFDAB9)
    }

    val PEAR = registerIngredient("pear") {
        JamIngredient("pear", listOf(), 0xD1E231)
    }

    val PINEAPPLE = registerIngredient("pineapple") {
        JamIngredient("pineapple", listOf(), 0xFCD116)
    }

    val RASPBERRY = registerIngredient("raspberry") {
        JamIngredient("raspberry", listOf(), 0xE30B5C)
    }

    val STRAWBERRY = registerIngredient("strawberry") {
        JamIngredient("strawberry", listOf(
            StatusEffectInstance(StatusEffects.ABSORPTION, 3600, 2)
        ), 0xFC5A8D)
    }

    fun registerJamIngredients() {
        FruitMod.logger.info("Registering Jam Ingredients for ${FruitMod.MOD_ID}")
    }

    private fun registerIngredient(
        name: String,
        factory: ((String) -> JamIngredient)
    ): RegistryEntry<JamIngredient> {
        val ingredient = factory(name)

        return Registry.registerReference(
            ModRegistries.JAM_INGREDIENT_REGISTRY,
            Identifier.of(FruitMod.MOD_ID, name),
            ingredient
        )
    }
}