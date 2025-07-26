package fruitmod.recipe

import fruitmod.component.ModDataComponents
import net.minecraft.item.ItemStack
import net.minecraft.recipe.CampfireCookingRecipe
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.CookingRecipeCategory
import net.minecraft.recipe.input.SingleStackRecipeInput
import net.minecraft.registry.RegistryWrapper

class JamBlockCampfireRecipe(
    group: String,
    cookingRecipeCategory: CookingRecipeCategory,
    ingredient: Ingredient,
    result: ItemStack,
    experience: Float,
    cookingTime: Int,
) : CampfireCookingRecipe(
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
}
