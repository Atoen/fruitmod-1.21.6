package fruitmod.recipe

import fruitmod.FruitMod
import fruitmod.util.modIdentifier
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModRecipeSerializers {



    fun registerSerializers() {
        FruitMod.logger.info("Registering Custom Recipe Serializers for ${FruitMod.MOD_ID}")
    }

    private fun <S : RecipeSerializer<T>, T : Recipe<*>> register(name: String, serializer: S) =
        Registry.register(
            Registries.RECIPE_SERIALIZER,
            modIdentifier(name),
            serializer
        )
}
