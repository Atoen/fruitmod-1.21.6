package fruitmod.item.jam

import fruitmod.FruitMod
import fruitmod.ModRegistries
import fruitmod.item.ModItems
import fruitmod.recipe.JamRecipeRegistry
import fruitmod.util.modIdentifier
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry

object JamIngredients {
    val BLUEBERRY = registerIngredient {
        JamIngredient(
            "blueberry",
            ModItems.BLUEBERRY,
            listOf(
                StatusEffectInstance(StatusEffects.NIGHT_VISION, 2400)
            ),
        0x4F42B5)
    }
    val CHERRIES = registerIngredient {
        JamIngredient(
            "cherries",
            ModItems.CHERRIES,
            listOf(StatusEffectInstance(StatusEffects.STRENGTH, 1800)),
            0xC21807
        )
    }

    val COCONUT = registerIngredient {
        JamIngredient(
            "coconut",
            ModItems.OPEN_COCONUT,
            listOf(StatusEffectInstance(StatusEffects.RESISTANCE, 1800)),
            0xA17C5B
        )
    }

    val GRAPES = registerIngredient {
        JamIngredient("grapes", ModItems.GRAPES, listOf(), 0x6B3FA0)
    }

    val HONEY_BERRIES = registerIngredient {
        JamIngredient("honey_berries", ModItems.HONEY_BERRIES, listOf(), 0x5D8AA8)
    }

    val KIWI = registerIngredient {
        JamIngredient(
            "kiwi",
            ModItems.KIWI,
            listOf(StatusEffectInstance(StatusEffects.JUMP_BOOST, 1800)),
            0x8DB600
        )
    }

    val LEMON_SLICE = registerIngredient {
        JamIngredient("lemon_slice", ModItems.LEMON_SLICE, listOf(), 0xFFF86C)
    }

    val LIME_SLICE = registerIngredient {
        JamIngredient("lime_slice", ModItems.LIME_SLICE, listOf(), 0xD4FF66)
    }

    val MANGO = registerIngredient {
        JamIngredient(
            "mango",
            ModItems.MANGO,
            listOf(StatusEffectInstance(StatusEffects.HASTE, 1800)),
            0xFFA62B
        )
    }

    val ORANGE = registerIngredient {
        JamIngredient(
            "orange",
            ModItems.ORANGE_SLICE,
            listOf(StatusEffectInstance(StatusEffects.SPEED, 3600)),
            0xFFA500
        )
    }

    val PAPAYA = registerIngredient {
        JamIngredient("papaya", ModItems.PAPAYA, listOf(), 0xFF7518)
    }

    val PEACH = registerIngredient {
        JamIngredient(
            "peach",
            ModItems.PEACH,
            listOf(StatusEffectInstance(StatusEffects.REGENERATION, 900)),
            0xFFDAB9
        )
    }

    val PEAR = registerIngredient {
        JamIngredient("pear", ModItems.PEAR, listOf(), 0xD1E231)
    }

    val PINEAPPLE = registerIngredient {
        JamIngredient("pineapple", ModItems.PINEAPPLE, listOf(), 0xFCD116)
    }

    val RASPBERRY = registerIngredient {
        JamIngredient(
            "raspberry",
            ModItems.RASPBERRY,
            listOf(StatusEffectInstance(StatusEffects.STRENGTH, 3600)),
            0xE30B5C
        )
    }

    val STRAWBERRY = registerIngredient {
        JamIngredient(
            "strawberry",
            ModItems.STRAWBERRY,
            listOf(StatusEffectInstance(StatusEffects.ABSORPTION, 3600, 2)),
            0xFC5A8D
        )
    }

    val APPLE = registerIngredient {
        JamIngredient("apple", Items.APPLE, listOf(), 0xFF0800)
    }

    val GOLDEN_APPLE = registerIngredient {
        JamIngredient(
            "golden_apple",
            Items.GOLDEN_APPLE,
            listOf(
                StatusEffectInstance(StatusEffects.REGENERATION, 100, 1),
                StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 0)
            ),
            0xFFD700
        )
    }

    val ENCHANTED_GOLDEN_APPLE = registerIngredient {
        JamIngredient(
            "enchanted_golden_apple",
            Items.ENCHANTED_GOLDEN_APPLE,
           listOf(
                StatusEffectInstance(StatusEffects.REGENERATION, 400, 1),
                StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3),
                StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0),
                StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000, 0)
            ),
            0xE6E8FA
        )
    }

    fun registerJamIngredients() {
        FruitMod.logger.info("Registering Jam Ingredients for {}", FruitMod.MOD_ID)
    }

    private fun registerIngredient(
        factory: () -> JamIngredient
    ): RegistryEntry<JamIngredient> {
        val ingredient = factory()

        return Registry.registerReference(
            ModRegistries.JAM_INGREDIENT,
            modIdentifier(ingredient.name),
            ingredient
        ).also {
            JamRecipeRegistry.registerIngredient(it)
        }
    }
}
