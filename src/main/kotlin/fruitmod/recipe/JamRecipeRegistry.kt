package fruitmod.recipe

import fruitmod.block.entity.JamCraftingInventory
import fruitmod.component.JamComponent
import fruitmod.component.ModDataComponents
import fruitmod.item.ModItems
import fruitmod.item.jam.Jam
import fruitmod.item.jam.JamIngredient
import fruitmod.item.jam.SpecialJam
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.registry.entry.RegistryEntry
import java.util.Optional

object JamRecipeRegistry {

    private val specialRecipes = mutableMapOf<JamRecipeKey, JamComponent>()
    private val recipes = mutableMapOf<JamRecipeKey, SpecialJam>()

    private val ingredientEntryMap = mutableMapOf<Item, RegistryEntry<JamIngredient>>()

    fun craft(inventory: JamCraftingInventory): ItemStack {

        if (!inventory.hasAllRequired) {
            return ItemStack.EMPTY
        }

        val key = JamRecipeKey.createKey(inventory.baseStack, inventory.ingredients)

        specialRecipes[key]?.let {
            return ItemStack(ModItems.JAM).apply {
                set(ModDataComponents.JAM, it.copy())
            }
        }

        val jamComponent = createGenericJamComponent(inventory.ingredients)
        if (jamComponent == null) {
            return ItemStack.EMPTY
        }

        return ItemStack(ModItems.JAM).apply {
            set(ModDataComponents.JAM, jamComponent)
        }
    }

    private fun createGenericJamComponent(ingredients: List<ItemStack>): JamComponent? {
        val registeredEntries = mapIngredientsToRegistryEntries(ingredients)

        if (registeredEntries.isEmpty()) {
            return null
        }

        return JamComponent(
            jam = Optional.empty(),
            ingredients = registeredEntries,
            customColor = Optional.empty(),
            additionalEffects = emptyList(),
            customName = Optional.empty(),
            portions = JamComponent.DEFAULT_PORTIONS
        )
    }

    fun addSpecialRecipe(recipe: RegistryEntry<SpecialJam>) {
        val key = recipe.value().createKey()
        recipes[key] = recipe.value()
    }

    fun registerSpecialRecipe(
        base: ItemConvertible,
        ingredients: List<ItemConvertible>,
        jam: RegistryEntry<Jam>,
        customColor: Optional<Int> = Optional.empty(),
        additionalEffects: List<StatusEffectInstance> = emptyList(),
        customName: Optional<String> = Optional.empty(),
        portions: Int = JamComponent.DEFAULT_PORTIONS
    ) {

        val key = JamRecipeKey.createKey(ItemStack(base), ingredients.map { ItemStack(it) })
        val registryEntries = mapIngredientsToRegistryEntries(ingredients.map { ItemStack(it) })

        val component = JamComponent(
            jam = Optional.of(jam),
            ingredients = registryEntries,
            customColor = customColor,
            additionalEffects = additionalEffects,
            customName = customName,
            portions = portions
        )

        specialRecipes[key] = component
    }

    fun registerIngredient(entry: RegistryEntry<JamIngredient>) {
        ingredientEntryMap[entry.value().item] = entry
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