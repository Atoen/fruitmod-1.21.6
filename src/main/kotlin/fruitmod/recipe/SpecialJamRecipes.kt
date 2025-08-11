package fruitmod.recipe

import fruitmod.item.ModItems
import fruitmod.item.jam.Jams
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Items
import java.util.Optional

object SpecialJamRecipes {
    init {
        JamRecipeRegistry.registerSpecialRecipe(
            base = Items.SUGAR,
            ingredients = listOf(Items.ENCHANTED_GOLDEN_APPLE, ModItems.PEAR),
            jam = Jams.GOLDEN_JAM,
            customColor = Optional.of(0xFFD700),
            additionalEffects = listOf(
                StatusEffectInstance(StatusEffects.REGENERATION, 600, 1),
                StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3)
            ),
            customName = Optional.of("Enchanted Pear Jam"),
            portions = 4
        )
    }

    fun call() {

    }
}