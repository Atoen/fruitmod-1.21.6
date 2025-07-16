package fruitmod.util

import fruitmod.component.ModDataComponents
import net.minecraft.component.ComponentType
import net.minecraft.item.DyeItem
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor

object JamColorHelper {
    fun normalizeColor(color: Color3i): Color3i {
        val (r, g, b) = color
        return normalizeColor(r, g, b)
    }

    fun normalizeColor(r: Int, g: Int, b: Int): Color3i {
        val max = maxOf(r, g, b).coerceAtLeast(1)
        val normR = (15.0 * r / max).toInt().coerceIn(0, 15)
        val normG = (15.0 * g / max).toInt().coerceIn(0, 15)
        val normB = (15.0 * b / max).toInt().coerceIn(0, 15)

        return Color3i(normR, normG, normB)
    }

    fun averageDyeColors(dyeStacks: List<ItemStack>): Color3i {
        var r = 0
        var g = 0
        var b = 0

        dyeStacks.forEach { dyeStack ->
            val dyeColor = (dyeStack.item as DyeItem).color
            val (dr, dg, db) = mapDyeColor(dyeColor)
            r += dr
            g += dg
            b += db
        }

        return normalizeColor(r, g, b)
    }

    fun averageJamBlockColors(jamBlockStacks: List<ItemStack>): Color3i {
        val color = sumColors(jamBlockStacks, ModDataComponents.JAM_BLOCK_COLOR)
        return normalizeColor(color)
    }

    fun averageJamsColors(jamBlockStacks: List<ItemStack>): Color3i {
        val color = sumColors(jamBlockStacks, ModDataComponents.JAM)
        return normalizeColor(color)
    }

    private fun sumColors(
        items: List<ItemStack>,
        type: ComponentType<out JamColorComponent>
    ): Color3i {
        var r = 0
        var g = 0
        var b = 0

        for (item in items) {
            val component = item.get(type) ?: continue
            val (cr, cg, cb) = component.channels
            r += cr
            g += cg
            b += cb
        }

        return Color3i(r, g, b)
    }

    fun mapDyeColor(dyeColor: DyeColor): Color3i {
        return DYE_COLOR_MAP[dyeColor] ?: EMPTY
    }

    private val EMPTY = Color3i(0, 0, 0)
    private val DYE_COLOR_MAP = mapOf(
        DyeColor.WHITE to Color3i(15, 15, 15),
        DyeColor.ORANGE to Color3i(15, 10, 0),
        DyeColor.MAGENTA to Color3i(13, 0, 13),
        DyeColor.LIGHT_BLUE to Color3i(5, 5, 15),
        DyeColor.YELLOW to Color3i(15, 15, 0),
        DyeColor.LIME to Color3i(5, 15, 5),
        DyeColor.PINK to Color3i(15, 8, 10),
        DyeColor.GRAY to Color3i(7, 7, 7),
        DyeColor.LIGHT_GRAY to Color3i(10, 10, 10),
        DyeColor.CYAN to Color3i(0, 10, 10),
        DyeColor.PURPLE to Color3i(10, 0, 10),
        DyeColor.BLUE to Color3i(0, 0, 15),
        DyeColor.BROWN to Color3i(10, 6, 2),
        DyeColor.GREEN to Color3i(0, 12, 0),
        DyeColor.RED to Color3i(15, 0, 0),
        DyeColor.BLACK to EMPTY
    )
}
