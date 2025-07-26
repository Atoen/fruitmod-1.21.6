package fruitmod.recipe

import fruitmod.FruitMod
import fruitmod.util.modIdentifier
import net.minecraft.recipe.AbstractCookingRecipe
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModRecipes {

    val JAM_BLOCK_COLOR = register("jam_block_color",
        SpecialCraftingRecipe.SpecialRecipeSerializer(::JamBlockColorRecipe)
    )

    val SOLID_JAM_BLOCK_COLOR = register("solid_jam_block_color",
        SpecialCraftingRecipe.SpecialRecipeSerializer(::SolidJamBlockColorRecipe)
    )

    val JAM_BLOCK = register("jam_block",
        SpecialCraftingRecipe.SpecialRecipeSerializer(::JamBlockRecipe))

    val JAM_BLOCK_SMELTING = JamBlockCookingRecipeType.FURNACE.run {
        register(id, createSerializer())
    }

    val JAM_BLOCK_SMOKING = JamBlockCookingRecipeType.SMOKER.run {
        register(id, createSerializer())
    }

    val JAM_BLOCK_CAMPFIRE = register("jam_block_campfire",
        AbstractCookingRecipe.Serializer(::JamBlockCampfireRecipe, 400))

    fun registerRecipes() {
        FruitMod.logger.info("Registering Custom Recipes for ${FruitMod.MOD_ID}")

        ModRecipeSerializers.registerSerializers()
        ModRecipeBookCategories.registerRecipeBookCategories()
    }

    private fun <S : RecipeSerializer<T>, T : Recipe<*>> register(name: String, serializer: S): S {
        val id = modIdentifier(name)
        return Registry.register(Registries.RECIPE_SERIALIZER, id, serializer)
    }
}
