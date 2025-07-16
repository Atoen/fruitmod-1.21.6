package fruitmod.recipe

import fruitmod.component.JamBlockColorComponent
import fruitmod.component.ModDataComponents
import fruitmod.item.ModItems
import fruitmod.item.custom.JamBlockItem
import fruitmod.util.JamColorHelper
import net.minecraft.item.DyeItem
import net.minecraft.item.ItemStack
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.recipe.input.CraftingRecipeInput
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.World

class JamBlockColorRecipe(
    category: CraftingRecipeCategory
) : SpecialCraftingRecipe(category) {

    override fun matches(
        input: CraftingRecipeInput,
        world: World
    ): Boolean {

        var jamBlockCount = 0
        var dyeFound = false

        for (stack in input.stacks) {
            if (stack.isEmpty) continue

            when (stack.item) {
                is JamBlockItem -> jamBlockCount++
                is DyeItem -> dyeFound = true
                else -> return false
            }
        }

        return if (dyeFound) {
            jamBlockCount >= 1
        } else {
            jamBlockCount >= 2
        }
    }

    override fun craft(
        input: CraftingRecipeInput,
        registries: RegistryWrapper.WrapperLookup
    ): ItemStack {
        val jamBlockStacks = mutableListOf<ItemStack>()
        val dyeStacks = mutableListOf<ItemStack>()

        for (it in input.stacks) {
            if (it.isEmpty) continue

            when (it.item) {
                is JamBlockItem -> jamBlockStacks.add(it)
                is DyeItem -> dyeStacks.add(it)
            }
        }

        if (jamBlockStacks.isEmpty()) {
            return ItemStack.EMPTY
        }

        val result = ItemStack(ModItems.JAM_BLOCK_ITEM, jamBlockStacks.size)
        val color = if (dyeStacks.isNotEmpty()) {
            JamColorHelper.averageDyeColors(dyeStacks)
        } else {
            JamColorHelper.averageJamBlockColors(jamBlockStacks)
        }

        result.set(ModDataComponents.JAM_BLOCK_COLOR, JamBlockColorComponent.fromColor3i(color))
        return result
    }

    override fun getSerializer() = ModRecipes.JAM_BLOCK_COLOR
}
