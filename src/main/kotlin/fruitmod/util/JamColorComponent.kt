package fruitmod.util

import net.minecraft.util.math.ColorHelper

interface JamColorComponent {
    val channels: Color3i
}

data class Color3i(val r: Int, val g: Int, val b: Int) {

    fun toLimitedColorSpace(): Color3i = Color3i(
        (r * 15 / 255).coerceIn(0, 15),
        (g * 15 / 255).coerceIn(0, 15),
        (b * 15 / 255).coerceIn(0, 15)
    )

    fun toFullColorSpace(): Color3i = Color3i(
        (r * 255 / 15).coerceIn(0, 255),
        (g * 255 / 15).coerceIn(0, 255),
        (b * 255 / 15).coerceIn(0, 255)
    )

    fun toInt() = ColorHelper.getArgb(r, g, b)

    companion object {
        fun fromRGB(color: Int) = Color3i(
            ColorHelper.getRed(color),
            ColorHelper.getGreen(color),
            ColorHelper.getBlue(color)
        )
    }
}