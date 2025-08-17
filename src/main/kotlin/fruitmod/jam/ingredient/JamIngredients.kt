package fruitmod.jam.ingredient

import fruitmod.FruitMod
import fruitmod.ModRegistries
import fruitmod.item.ModItems
import fruitmod.jam.recipe.JamRecipeRegistry
import fruitmod.jam.trait.JamTrait
import fruitmod.jam.trait.RandomTeleport
import fruitmod.util.modIdentifier
import fruitmod.util.toRegistryEntry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import java.util.Optional

object JamIngredients {
    val BLUEBERRY = registerIngredient(
        "blueberry", ModItems.BLUEBERRY,
        listOf(StatusEffectInstance(StatusEffects.NIGHT_VISION, 2400)),
        0x4F42B5
    )

    val CHORUS_FRUIT = registerIngredient(
        "chorus_fruit", Items.CHORUS_FRUIT,
        trait = RandomTeleport(),
        color = 0x4F42B5
    )

    val CHERRIES = registerIngredient(
        "cherries", ModItems.CHERRIES,
        listOf(StatusEffectInstance(StatusEffects.STRENGTH, 1800)),
        0xC21807
    )

    val COCONUT = registerIngredient(
        "coconut", ModItems.OPEN_COCONUT,
        listOf(StatusEffectInstance(StatusEffects.RESISTANCE, 1800)),
        0xA17C5B
    )

    val GRAPES = registerIngredient("grapes", ModItems.GRAPES, color = 0x6B3FA0)

    val HONEY_BERRIES = registerIngredient("honey_berries", ModItems.HONEY_BERRIES, color = 0x5D8AA8)

    val KIWI = registerIngredient(
        "kiwi", ModItems.KIWI,
        listOf(StatusEffectInstance(StatusEffects.JUMP_BOOST, 1800)),
        0x8DB600
    )

    val LEMON_SLICE = registerIngredient("lemon_slice", ModItems.LEMON_SLICE, color = 0xFFF86C)

    val LIME_SLICE = registerIngredient("lime_slice", ModItems.LIME_SLICE, color = 0xD4FF66)

    val MANGO = registerIngredient(
        "mango", ModItems.MANGO,
        listOf(StatusEffectInstance(StatusEffects.HASTE, 1800)),
        0xFFA62B
    )

    val ORANGE = registerIngredient(
        "orange", ModItems.ORANGE_SLICE,
        listOf(StatusEffectInstance(StatusEffects.SPEED, 3600)),
        0xFFA500
    )

    val PAPAYA = registerIngredient("papaya", ModItems.PAPAYA, color = 0xFF7518)

    val PEACH = registerIngredient(
        "peach", ModItems.PEACH,
        listOf(StatusEffectInstance(StatusEffects.REGENERATION, 900)),
        0xFFDAB9
    )

    val PEAR = registerIngredient("pear", ModItems.PEAR, color = 0xD1E231)

    val PINEAPPLE = registerIngredient("pineapple", ModItems.PINEAPPLE, color = 0xFCD116)

    val RASPBERRY = registerIngredient(
        "raspberry", ModItems.RASPBERRY,
        listOf(StatusEffectInstance(StatusEffects.STRENGTH, 3600)),
        0xE30B5C
    )

    val STRAWBERRY = registerIngredient(
        "strawberry", ModItems.STRAWBERRY,
        listOf(StatusEffectInstance(StatusEffects.ABSORPTION, 3600, 2)),
        0xFC5A8D
    )

    val APPLE = registerIngredient("apple", Items.APPLE, color = 0xFF0800)

    val GOLDEN_APPLE = registerIngredient(
        "golden_apple", Items.GOLDEN_APPLE,
        listOf(
            StatusEffectInstance(StatusEffects.REGENERATION, 100, 1),
            StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 0)
        ),
        0xFFD700
    )

    val ENCHANTED_GOLDEN_APPLE = registerIngredient(
        "enchanted_golden_apple", Items.ENCHANTED_GOLDEN_APPLE,
        listOf(
            StatusEffectInstance(StatusEffects.REGENERATION, 400, 1),
            StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3),
            StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0),
            StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000, 0)
        ),
        0xE6E8FA
    )

    fun registerJamIngredients() {
        FruitMod.logger.info("Registering Jam Ingredients for {}", FruitMod.MOD_ID)
    }

    private fun registerIngredient(
        id: String,
        item: Item,
        effects: List<StatusEffectInstance> = emptyList(),
        color: Int,
        trait: JamTrait? = null,
        customNameKey: String? = null
    ): RegistryEntry<JamIngredient> {
        val ingredient = JamIngredient(
            item.toRegistryEntry(),
            effects, color,
            Optional.ofNullable(customNameKey),
            Optional.ofNullable(trait)
        )

        return Registry.registerReference(
            ModRegistries.JAM_INGREDIENT,
            modIdentifier(id),
            ingredient
        ).also {
            JamRecipeRegistry.registerIngredient(item, it)
        }
    }
}
