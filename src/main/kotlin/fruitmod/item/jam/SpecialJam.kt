package fruitmod.item.jam

import com.mojang.serialization.Codec
import fruitmod.ModRegistries
import fruitmod.block.entity.JamCraftingInventory
import fruitmod.component.JamComponent
import fruitmod.recipe.JamRecipeKey
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Rarity
import java.util.Optional

data class SpecialJam(
    val translationKey: String,
    val base: Ingredient,
    val ingredients: List<Ingredient>,
    val additionalEffects: List<StatusEffectInstance>,
    val customColor: Optional<Int> = Optional.empty(),
    val portions: Int = JamComponent.Companion.DEFAULT_PORTIONS,
    val ignoreIngredientEffects: Boolean = false,
    val hasGlint: Boolean = false,
    val rarity: Rarity = Rarity.UNCOMMON
) {

    fun canBeCrafted(inventory: JamCraftingInventory): Boolean {
        if (!base.test(inventory.baseStack)) {
            return false
        }

        val invIngredients = inventory.ingredients
        val used = BooleanArray(invIngredients.size) { false }

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

    companion object {
        val CODEC: Codec<RegistryEntry<SpecialJam>> = ModRegistries.SPECIAL_JAM.entryCodec
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, RegistryEntry<SpecialJam>> =
            PacketCodecs.registryEntry(ModRegistries.SPECIAL_JAM_REGISTRY_KEY)
    }
}
