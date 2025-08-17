package fruitmod.jam

import fruitmod.jam.trait.TraitTypes
import fruitmod.jam.base.JamBases
import fruitmod.jam.trait.JamTrait
import fruitmod.jam.ingredient.JamIngredients
import fruitmod.jam.recipe.SpecialJamRecipes

fun registerJamFunctionality() {
    JamTrait.registerJamTraits()
    TraitTypes.registerTraitTypes()
    JamBases.registerJamBases()
    JamIngredients.registerJamIngredients()
    SpecialJamRecipes.registerJams()
}