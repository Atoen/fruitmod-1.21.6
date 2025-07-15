package fruitmod.recipe

import fruitmod.component.ModDataComponents
import fruitmod.item.ModItems
import fruitmod.item.custom.SolidJamBlockItem
import net.minecraft.item.DyeItem
import net.minecraft.item.ItemStack
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.recipe.input.CraftingRecipeInput
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.World

class SolidJamBlockColorRecipe (
    category: CraftingRecipeCategory
) : SpecialCraftingRecipe(category) {

    override fun matches(
        input: CraftingRecipeInput,
        world: World
    ): Boolean {

        var solidJamBlockCount = 0
        var dyeFound = false

        for (stack in input.stacks) {
            if (stack.isEmpty) continue

            when (stack.item) {
                is SolidJamBlockItem -> solidJamBlockCount++
                is DyeItem -> dyeFound = true
                else -> return false
            }
        }

        return if (dyeFound) {
            solidJamBlockCount >= 1
        } else {
            solidJamBlockCount >= 2
        }
    }

    override fun craft(
        input: CraftingRecipeInput,
        registries: RegistryWrapper.WrapperLookup
    ): ItemStack {
        val solidJamBlockStacks = mutableListOf<ItemStack>()
        val dyeStacks = mutableListOf<ItemStack>()

        for (it in input.stacks) {
            if (it.isEmpty) continue

            when (it.item) {
                is SolidJamBlockItem -> solidJamBlockStacks.add(it)
                is DyeItem -> dyeStacks.add(it)
            }
        }

        if (solidJamBlockStacks.isEmpty()) {
            return ItemStack.EMPTY
        }

        val result = ItemStack(ModItems.SOLID_JAM_BLOCK_ITEM, solidJamBlockStacks.size)
        val color = if (dyeStacks.isNotEmpty()) {
            JamBlockColorRecipe.averageDyeColors(dyeStacks)
        } else {
            JamBlockColorRecipe.averageJamBlockColors(solidJamBlockStacks)
        }

        result.set(ModDataComponents.JAM_BLOCK_COLOR, color)
        return result
    }

    override fun getSerializer() = ModRecipes.SOLID_JAM_BLOCK_COLOR
}
