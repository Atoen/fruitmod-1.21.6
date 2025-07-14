package fruitmod.block

import net.minecraft.state.property.IntProperty

object JamBlockProperties {
    val RED: IntProperty = IntProperty.of("red", 0, 15)
    val GREEN: IntProperty = IntProperty.of("green", 0, 15)
    val BLUE: IntProperty = IntProperty.of("blue", 0, 15)
}