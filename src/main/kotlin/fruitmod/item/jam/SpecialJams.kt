package fruitmod.item.jam

import fruitmod.FruitMod
import fruitmod.ModRegistries
import fruitmod.ModTags
import fruitmod.recipe.JamRecipeRegistry
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
import java.util.*

object SpecialJams {

    val GOLDEN_JAM = registerJam("golden") {
        SpecialJam(
            translationKey = "fruitmod.jams.golden",
            base = Ingredient.ofItem(Items.SUGAR),
            ingredients = listOf(
                Ingredient.ofItem(Items.GOLDEN_APPLE),
                ingredientFromTag(ModTags.Items.JAM_INGREDIENT)
            ),
            additionalEffects = listOf(
                StatusEffectInstance(StatusEffects.ABSORPTION, 1800, 2),
                StatusEffectInstance(StatusEffects.REGENERATION, 300, 2)
            ),
            customColor = Optional.of(0xFFD700),
            hasGlint = true
        )
    }

    fun registerJams() {
        FruitMod.logger.info("Registering Special Jams for {}", FruitMod.MOD_ID)
    }

    private fun registerJam(name: String, factory: () -> SpecialJam): RegistryEntry<SpecialJam> {
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