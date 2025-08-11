package fruitmod.block.entity

import net.minecraft.item.ItemStack

interface JamCraftingInventory {
    val baseStack: ItemStack
    val ingredients: List<ItemStack>
    val jarStack: ItemStack
    var outputStack: ItemStack

    val hasAllRequired get(): Boolean =
        !baseStack.isEmpty && !jarStack.isEmpty && ingredients.any { !it.isEmpty }

    fun consumeIngredients() {
        baseStack.decrement(1)
        ingredients.forEach { if (!it.isEmpty) it.decrement(1) }
        jarStack.decrement(1)
    }

    fun setOutput(result: ItemStack) {
        outputStack = result.copy()
    }

    fun clearOutput() {
        outputStack = ItemStack.EMPTY
    }
}