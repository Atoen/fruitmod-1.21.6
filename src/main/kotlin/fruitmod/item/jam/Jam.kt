package fruitmod.item.jam

import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance

class Jam(val name: String, val ingredients: List<JamIngredient>) {

    val combinedEffects: List<StatusEffectInstance> = combineEffects()

    constructor(name: String, vararg ingredients: JamIngredient) : this(name, ingredients.toList())

    private fun combineEffects(): List<StatusEffectInstance> {
        val effectMap: MutableMap<StatusEffect, StatusEffectInstance> = mutableMapOf<StatusEffect, StatusEffectInstance>()

        return effectMap.values.toList()
    }
}

data class JamIngredient(val name: String, val effects: List<StatusEffectInstance>)