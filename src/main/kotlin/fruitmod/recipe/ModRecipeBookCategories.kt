package fruitmod.recipe

import fruitmod.FruitMod
import fruitmod.util.modIdentifier
import net.minecraft.recipe.book.RecipeBookCategory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModRecipeBookCategories {

    val SMOKER_BLOCKS = register("smoker_blocks")

    fun registerRecipeBookCategories() {
        FruitMod.logger.info("Registering Custom Recipe Book Categories for ${FruitMod.MOD_ID}")
    }

    private fun register(name: String) = Registry.register(
        Registries.RECIPE_BOOK_CATEGORY,
        modIdentifier(name),
        RecipeBookCategory()
    )
}