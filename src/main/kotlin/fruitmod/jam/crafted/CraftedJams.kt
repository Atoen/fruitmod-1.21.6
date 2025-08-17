package fruitmod.jam.crafted

import fruitmod.jam.ingredient.JamIngredients

object CraftedJams {
    val APPLE = CraftedJam.Companion.sugarBase(
        listOf(JamIngredients.APPLE)
    )

    val PEAR = CraftedJam.Companion.sugarBase(
        listOf(JamIngredients.PEAR)
    )

    val ORANGE = CraftedJam.Companion.sugarBase(
        listOf(JamIngredients.ORANGE)
    )

    val GRAPES = CraftedJam.Companion.sugarBase(
        listOf(JamIngredients.GRAPES)
    )

    val PEACH = CraftedJam.Companion.sugarBase(
        listOf(JamIngredients.PEACH)
    )

    val ALL = listOf(APPLE, PEAR, ORANGE, GRAPES, PEACH)
}
