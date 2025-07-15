package fruitmod.recipe

import fruitmod.component.JamBlockColorComponent
import fruitmod.component.ModDataComponents
import fruitmod.item.ModItems
import fruitmod.item.custom.JamBlockItem
import net.minecraft.item.DyeItem
import net.minecraft.item.ItemStack
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.recipe.input.CraftingRecipeInput
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.DyeColor
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
            averageDyeColors(dyeStacks)
        } else {
            averageJamBlockColors(jamBlockStacks)
        }

        result.set(ModDataComponents.JAM_BLOCK_COLOR, color)
        return result
    }

    override fun getSerializer() = ModRecipes.JAM_BLOCK_COLOR

    internal companion object {

        fun normalizeColor(r: Int, g: Int, b: Int): JamBlockColorComponent {
            val max = maxOf(r, g, b).coerceAtLeast(1)
            val normR = (15.0 * r / max).toInt().coerceIn(0, 15)
            val normG = (15.0 * g / max).toInt().coerceIn(0, 15)
            val normB = (15.0 * b / max).toInt().coerceIn(0, 15)
            return JamBlockColorComponent(normR, normG, normB)
        }

        fun averageDyeColors(dyeStacks: List<ItemStack>): JamBlockColorComponent {
            var r = 0
            var g = 0
            var b = 0

            dyeStacks.forEach { dyeStack ->
                val dyeColor = (dyeStack.item as DyeItem).color
                val (dr, dg, db) = mapDyeColor(dyeColor)
                r += dr
                g += dg
                b += db
            }

            return normalizeColor(r, g, b)
        }

        fun averageJamBlockColors(jamBlockStacks: List<ItemStack>): JamBlockColorComponent {
            var totalRed = 0
            var totalGreen = 0
            var totalBlue = 0

            jamBlockStacks.forEach {
                val color = it.getOrDefault(ModDataComponents.JAM_BLOCK_COLOR, JamBlockColorComponent.EMPTY)
                totalRed += color.red
                totalGreen += color.green
                totalBlue += color.blue
            }

            return normalizeColor(totalRed, totalGreen, totalBlue)
        }

        fun mapDyeColor(dyeColor: DyeColor): Triple<Int, Int, Int> {
            return when (dyeColor) {
                DyeColor.WHITE -> Triple(15, 15, 15)
                DyeColor.ORANGE -> Triple(15, 10, 0)
                DyeColor.MAGENTA -> Triple(13, 0, 13)
                DyeColor.LIGHT_BLUE -> Triple(5, 5, 15)
                DyeColor.YELLOW -> Triple(15, 15, 0)
                DyeColor.LIME -> Triple(5, 15, 5)
                DyeColor.PINK -> Triple(15, 8, 10)
                DyeColor.GRAY -> Triple(7, 7, 7)
                DyeColor.LIGHT_GRAY -> Triple(10, 10, 10)
                DyeColor.CYAN -> Triple(0, 10, 10)
                DyeColor.PURPLE -> Triple(10, 0, 10)
                DyeColor.BLUE -> Triple(0, 0, 15)
                DyeColor.BROWN -> Triple(10, 6, 2)
                DyeColor.GREEN -> Triple(0, 12, 0)
                DyeColor.RED -> Triple(15, 0, 0)
                DyeColor.BLACK -> Triple(0, 0, 0)
            }
        }
    }
}
