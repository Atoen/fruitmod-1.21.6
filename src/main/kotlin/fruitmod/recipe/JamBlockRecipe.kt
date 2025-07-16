package fruitmod.recipe

import fruitmod.component.JamBlockColorComponent
import fruitmod.component.ModDataComponents
import fruitmod.item.ModItems
import fruitmod.item.custom.JamItem
import fruitmod.util.JamColorHelper
import net.minecraft.item.ItemStack
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.recipe.input.CraftingRecipeInput
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.World

class JamBlockRecipe(
    category: CraftingRecipeCategory
) : SpecialCraftingRecipe(category) {

    override fun matches(
        input: CraftingRecipeInput,
        world: World
    ): Boolean {
        var jamCount = 0

        for (stack in input.stacks) {
            if (stack.isEmpty) continue

            if (stack.item is JamItem) jamCount++
            else return false
        }

        return jamCount > 0
    }

    override fun craft(
        input: CraftingRecipeInput,
        registries: RegistryWrapper.WrapperLookup
    ): ItemStack {
        val jamStacks = input.stacks.filter {
            it.item is JamItem
        }

        if (jamStacks.isEmpty()) {
            return ItemStack.EMPTY
        }

        val result = ItemStack(ModItems.JAM_BLOCK_ITEM, jamStacks.size * JAM_BLOCKS_PER_JAM_ITEM)
        val color = JamColorHelper.averageJamsColors(jamStacks)

        result.set(ModDataComponents.JAM_BLOCK_COLOR, JamBlockColorComponent.fromColor3i(color))
        return result
    }

    override fun getSerializer() = ModRecipes.JAM_BLOCK

    companion object {
        const val JAM_BLOCKS_PER_JAM_ITEM = 2
    }
}