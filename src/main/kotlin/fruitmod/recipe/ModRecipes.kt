package fruitmod.recipe

import fruitmod.FruitMod
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModRecipes {

    val JAM_MERGING_SERIALIZER = Registry.register(
        Registries.RECIPE_SERIALIZER, Identifier.of(FruitMod.MOD_ID, "jam_merging"),
        JamMergingRecipe.Serializer()
    )

    fun registerRecipes() {
        FruitMod.logger.info("Registering Custom Recipes for ${FruitMod.MOD_ID}")
    }
}