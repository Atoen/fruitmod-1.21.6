package fruitmod.jam.recipe

import fruitmod.block.entity.JamCraftingInventory
import fruitmod.component.JamComponent
import fruitmod.component.ModDataComponents
import fruitmod.item.ModItems
import fruitmod.jam.base.JamBase
import fruitmod.jam.crafted.CraftedJam
import fruitmod.jam.ingredient.JamIngredient
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.registry.entry.RegistryEntry

object JamRecipeRegistry {

    private val specialRecipes = mutableMapOf<JamRecipeKey, JamComponent>()
    private val recipes = mutableListOf<SpecialJamRecipe>()

    val ingredientEntryMap = mutableMapOf<Item, RegistryEntry<JamIngredient>>()
    private val baseEntryMap = mutableMapOf<Item, RegistryEntry<JamBase>>()

    fun craft(inventory: JamCraftingInventory): ItemStack {

        if (!inventory.hasAllRequired) {
            return ItemStack.EMPTY
        }

//        val key = JamRecipeKey.createKey(inventory.baseStack, inventory.ingredients)

//        specialRecipes[key]?.let {
//            return ItemStack(ModItems.JAM).apply {
//                set(ModDataComponents.JAM, it.copy())
//            }
//        }

        recipes.firstOrNull { it.canBeCrafted(inventory) }?.let {
            val crafted = it.craft(inventory)
            return crafted!!
        }

        val craftedJam = createGenericJam(inventory)
        if (craftedJam == null) {
            return ItemStack.EMPTY
        }

        return ItemStack(ModItems.JAM).apply {
            set(ModDataComponents.JAM, JamComponent(craftedJam))
        }
    }

    private fun createGenericJam(inventory: JamCraftingInventory): CraftedJam? {
        val base = baseEntryMap[inventory.baseStack.item] ?: return null

        val registeredEntries = mapIngredientsToRegistryEntries(inventory.ingredients)
        if (registeredEntries.isEmpty()) {
            return null
        }

        return CraftedJam(base, registeredEntries)
    }

    fun addSpecialRecipe(recipe: RegistryEntry<SpecialJamRecipe>) {
        recipes.add(recipe.value())

//        val key = recipe.value().createKey()
//        recipes[key] = recipe.value()
    }

    fun registerIngredient(item: Item, entry: RegistryEntry<JamIngredient>) {
        ingredientEntryMap[item] = entry
    }

    fun registerBase(item: Item, entry: RegistryEntry<JamBase>) {
        baseEntryMap[item] = entry
    }

    private fun mapIngredientsToRegistryEntries(
        stacks: List<ItemStack>
    ): List<RegistryEntry<JamIngredient>> {
        return stacks.mapNotNull { ingredientEntryMap[it.item] }
    }

    class Builder(private val base: ItemConvertible) {
        private val entries = mutableListOf<Pair<List<ItemConvertible>, ItemStack>>()

        fun recipe(vararg ingredients: ItemConvertible, result: ItemStack): Builder {
            entries += ingredients.toList() to result
            return this
        }
    }
}