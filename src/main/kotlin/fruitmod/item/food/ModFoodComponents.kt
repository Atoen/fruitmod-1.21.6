package fruitmod.item.food

import net.minecraft.component.type.FoodComponent

object ModFoodComponents {
    val BASIC_FRUIT = FoodComponent.Builder()
        .nutrition(2)
        .saturationModifier(0.3f)
        .build()

    val HIGH_NUTRITION_FRUIT = FoodComponent.Builder()
        .nutrition(5)
        .saturationModifier(0.5f)
        .build()

    val LOW_NUTRITION_FRUIT = FoodComponent.Builder()
        .nutrition(1)
        .saturationModifier(0.1f)
        .build()

    val JUICY_FRUIT = FoodComponent.Builder()
        .nutrition(3)
        .saturationModifier(0.4f)
        .build()

    val SLICE = FoodComponent.Builder()
        .nutrition(2)
        .saturationModifier(0.1f)
        .build()
}