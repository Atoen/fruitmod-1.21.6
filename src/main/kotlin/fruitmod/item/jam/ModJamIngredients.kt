package fruitmod.item.jam

import fruitmod.FruitMod
import fruitmod.ModRegistries
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

object ModJamIngredients {
    val STRAWBERRY = registerIngredient("strawberry") {
        JamIngredient("Strawberry", listOf(StatusEffectInstance(StatusEffects.SPEED, 20 * 60, 1)))
    }

    val BLUEBERRY = registerIngredient("blueberry") {
        JamIngredient("Blueberry", listOf(StatusEffectInstance(StatusEffects.NIGHT_VISION, 20 * 120, 1)))
    }

    fun registerJamIngredients() {
        FruitMod.logger.info("Registering Jam Ingredients for ${FruitMod.MOD_ID}")
    }

    private fun registerIngredient(
        name: String,
        factory: (() -> JamIngredient)
    ): JamIngredient {
        val key = RegistryKey.of(ModRegistries.JAM_INGREDIENT_REGISTRY_KEY, Identifier.of(FruitMod.MOD_ID, name))
        val ingredient = factory()

        return Registry.register(ModRegistries.JAM_INGREDIENT_REGISTRY, key, ingredient)
    }
}