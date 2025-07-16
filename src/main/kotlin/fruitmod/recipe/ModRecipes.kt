package fruitmod.recipe

import fruitmod.FruitMod
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModRecipes {

    val JAM_BLOCK_COLOR = register("jam_block_color",
        SpecialCraftingRecipe.SpecialRecipeSerializer(::JamBlockColorRecipe)
    )

    val SOLID_JAM_BLOCK_COLOR = register("solid_jam_block_color",
        SpecialCraftingRecipe.SpecialRecipeSerializer(::SolidJamBlockColorRecipe)
    )

    val JAM_BLOCK = register("jam_block",
        SpecialCraftingRecipe.SpecialRecipeSerializer(::JamBlockRecipe))

    fun registerRecipes() {
        FruitMod.logger.info("Registering Custom Recipes for ${FruitMod.MOD_ID}")
    }

    private fun <S : RecipeSerializer<T>, T : Recipe<*>> register(name: String, serializer: S): S {
        val id = Identifier.of(FruitMod.MOD_ID, name)
        return Registry.register(Registries.RECIPE_SERIALIZER, id, serializer)
    }
}