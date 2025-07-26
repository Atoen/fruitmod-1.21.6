package fruitmod.recipe

import fruitmod.component.ModDataComponents
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.AbstractCookingRecipe
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.book.CookingRecipeCategory
import net.minecraft.recipe.book.RecipeBookCategories
import net.minecraft.recipe.book.RecipeBookCategory
import net.minecraft.recipe.input.SingleStackRecipeInput
import net.minecraft.registry.RegistryWrapper

class JamBlockCookingRecipe private constructor(
    group: String,
    cookingRecipeCategory: CookingRecipeCategory,
    ingredient: Ingredient,
    result: ItemStack,
    experience: Float,
    cookingTime: Int,
    val type: JamBlockCookingRecipeType
) : AbstractCookingRecipe(
    group,
    cookingRecipeCategory,
    ingredient,
    result,
    experience,
    cookingTime
) {

    override fun craft(
        input: SingleStackRecipeInput,
        wrapperLookup: RegistryWrapper.WrapperLookup
    ): ItemStack {
        val output = result().copy()

         input.item.get(ModDataComponents.JAM_BLOCK_COLOR)?.run {
            output.set(ModDataComponents.JAM_BLOCK_COLOR, this)
        }

        return output
    }

    override fun getSerializer(): Serializer<JamBlockCookingRecipe> = when (type) {
        JamBlockCookingRecipeType.FURNACE -> ModRecipes.JAM_BLOCK_SMELTING
        JamBlockCookingRecipeType.SMOKER -> ModRecipes.JAM_BLOCK_SMOKING
    }

    override fun getType(): RecipeType<out AbstractCookingRecipe> = when (type) {
        JamBlockCookingRecipeType.FURNACE -> RecipeType.SMELTING
        JamBlockCookingRecipeType.SMOKER -> RecipeType.SMOKING
    }

    override fun getCookerItem(): Item = when (type) {
        JamBlockCookingRecipeType.FURNACE -> Items.FURNACE
        JamBlockCookingRecipeType.SMOKER -> Items.SMOKER
    }

    override fun getRecipeBookCategory(): RecipeBookCategory = when (type) {
        JamBlockCookingRecipeType.FURNACE -> RecipeBookCategories.FURNACE_BLOCKS
        JamBlockCookingRecipeType.SMOKER -> ModRecipeBookCategories.SMOKER_BLOCKS
    }

    companion object {
        fun create(
            type: JamBlockCookingRecipeType,
            group: String,
            category: CookingRecipeCategory,
            ingredient: Ingredient,
            result: ItemStack,
            experience: Float,
            cookingTime: Int,
        ): JamBlockCookingRecipe {
            return JamBlockCookingRecipe(group, category, ingredient, result, experience, cookingTime, type)
        }
    }
}

enum class JamBlockCookingRecipeType(
    val defaultCookingTime: Int,
    val id: String
) {
    FURNACE(200, "jam_block_smelting"),
    SMOKER(100, "jam_block_smoking");

    val factory: (String, CookingRecipeCategory, Ingredient, ItemStack, Float, Int) -> JamBlockCookingRecipe =
        { group, category, ingredient, result, experience, cookingTime ->
            JamBlockCookingRecipe.create(this, group, category, ingredient, result, experience, cookingTime)
        }

    fun createSerializer() = AbstractCookingRecipe.Serializer(factory, defaultCookingTime)
}
