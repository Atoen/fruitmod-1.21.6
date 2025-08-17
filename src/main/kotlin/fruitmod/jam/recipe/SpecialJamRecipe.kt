package fruitmod.jam.recipe

import com.mojang.serialization.Codec
import fruitmod.ModRegistries
import fruitmod.block.entity.JamCraftingInventory
import fruitmod.jam.trait.JamTrait
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Rarity
import java.util.Optional

data class SpecialJamRecipe(
    val translationKey: String,
    val base: Ingredient,
    val ingredients: List<Ingredient>,
    val additionalEffects: List<StatusEffectInstance> = emptyList(),
    val additionalTraits: List<JamTrait> = emptyList(),
    val customColor: Optional<Int> = Optional.empty(),
    val ignoreIngredientEffects: Boolean = false,
    val rarity: Rarity = Rarity.UNCOMMON,
    val hasGlint: Boolean = rarity >= Rarity.RARE,
) {

    fun canBeCrafted(inventory: JamCraftingInventory): Boolean {
        if (!base.test(inventory.baseStack)) {
            return false
        }

        val invIngredients = inventory.ingredients.filter { !it.isEmpty }
        val used = BooleanArray(invIngredients.size)

        outer@for (required in ingredients) {
            for (i in invIngredients.indices) {
                val invStack = invIngredients[i]
                if (!used[i] && !invStack.isEmpty && required.test(invStack)) {
                    used[i] = true
                    continue@outer
                }
            }
            return false
        }

        return true
    }

    fun craft(inventory: JamCraftingInventory): ItemStack? {
        if (!canBeCrafted(inventory)) {
            return null
        }

        return null
//
//        // Deduct ingredients first
//        inventory.consumeIngredients()
//
//        // Base jam item
//        val baseEntry = inventory.baseStack.item.toRegistryEntry()
//
//        // Convert recipe ingredients to registry entries for JamIngredient
//        val ingredientEntries = ingredients.mapNotNull { recipeIng ->
//            inventory.ingredients.firstOrNull { stack ->
//                !stack.isEmpty && recipeIng.test(stack)
//            }?.item?.let { item ->
//                JamRecipeRegistry.ingredientEntryMap[item]
//            }
//        }
//
//        // Build effects
//        val effects = mutableListOf<StatusEffectInstance>()
//        effects += additionalEffects
//
//        if (!ignoreIngredientEffects) {
//            ingredientEntries.forEach { entry ->
//                effects += entry.value().effects // Assuming JamIngredient has `.effects`
//            }
//        }
//
//        // Build jam data
//        val craftedJam = CraftedJam(
////            base = baseEntry.value()
//            base = JamBases.SUGAR,
//            ingredients = ingredientEntries,
//            additionalEffects = effects,
//            customNameKey = Optional.of(translationKey),
//            customColor = customColor,
//            rarity = rarity,
//            hasGlint = hasGlint
//        )
//
//        // Create jam stack
//        val jamStack = ItemStack(ModItems.JAM)
//        jamStack.set(ModDataComponents.JAM, NewJamComponent(craftedJam))
//
//        return jamStack
    }

    companion object {
        val CODEC: Codec<RegistryEntry<SpecialJamRecipe>> = ModRegistries.SPECIAL_JAM.entryCodec
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, RegistryEntry<SpecialJamRecipe>> =
            PacketCodecs.registryEntry(ModRegistries.SPECIAL_JAM_REGISTRY_KEY)
    }
}