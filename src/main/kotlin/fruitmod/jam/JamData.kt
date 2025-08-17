package fruitmod.jam

import fruitmod.jam.base.JamBase
import fruitmod.jam.ingredient.JamIngredient
import fruitmod.jam.trait.JamTrait
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Rarity
import java.util.Optional

interface JamData {
    val base: RegistryEntry<JamBase>
    val ingredients: List<RegistryEntry<JamIngredient>>
    val additionalEffects: List<StatusEffectInstance>
    val traits: List<JamTrait>
    val customNameKey: Optional<String>
    val customColor: Optional<Int>
    val rarity: Rarity
    val hasGlint: Boolean
    val maxPortions: Int
}
