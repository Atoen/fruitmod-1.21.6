package fruitmod.jam.recipe

import fruitmod.FruitMod
import fruitmod.ModRegistries
import fruitmod.item.ModItems
import fruitmod.jam.recipe.JamRecipeRegistry
import fruitmod.util.modIdentifier
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Rarity
import java.util.*

object SpecialJamRecipes {

    val GOLDEN_JAM = registerRecipe("golden") {
        SpecialJamRecipe(
            translationKey = "fruitmod.jams.golden",
            base = Ingredient.ofItem(Items.SUGAR),
            ingredients = listOf(
                Ingredient.ofItem(Items.GOLDEN_APPLE),
//                ingredientFromTag(ModTags.Items.JAM_INGREDIENT)
            ),
            additionalEffects = listOf(
                StatusEffectInstance(StatusEffects.ABSORPTION, 1800, 2),
                StatusEffectInstance(StatusEffects.REGENERATION, 300, 2)
            ),
            customColor = Optional.of(0xFFD700),
            hasGlint = true
        )
    }

    val FRUITFUL_JAM = registerRecipe("fruitful") {
        SpecialJamRecipe(
            translationKey = "fruitmod.jams.fruitful",
            base = Ingredient.ofItem(Items.SUGAR),
            ingredients = listOf(
                Ingredient.ofItem(ModItems.ORANGE),
                Ingredient.ofItem(ModItems.BANANA),
                Ingredient.ofItem(ModItems.GRAPES),
                Ingredient.ofItem(ModItems.STRAWBERRY),
            ),
            additionalEffects = listOf(
                StatusEffectInstance(StatusEffects.NIGHT_VISION, 1800, 2),
            ),
            ignoreIngredientEffects = true,
            customColor = Optional.of(0xFFFFFF),
            rarity = Rarity.EPIC
        )
    }

    fun registerJams() {
        FruitMod.logger.info("Registering Special Jam Recipes for {}", FruitMod.MOD_ID)
    }

    private fun registerRecipe(name: String, factory: () -> SpecialJamRecipe): RegistryEntry<SpecialJamRecipe> {
        val jam = factory()
        return Registry.registerReference(
            ModRegistries.SPECIAL_JAM,
            modIdentifier(name),
            jam
        ).also { JamRecipeRegistry.addSpecialRecipe(it) }
    }
}

private fun ingredientFromTag(tag: TagKey<Item>): Ingredient {
    val registryEntryList = Registries.ITEM.getOptional(tag)
    assert(registryEntryList.isPresent)

    return Ingredient.ofTag(registryEntryList.get())
}