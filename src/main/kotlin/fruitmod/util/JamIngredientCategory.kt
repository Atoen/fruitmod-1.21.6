package fruitmod.util

import net.minecraft.util.Formatting

enum class JamIngredientCategory(val format: Formatting) {
    REGULAR(Formatting.GREEN),
    RARE(Formatting.GOLD),
    HARMFUL(Formatting.RED);
}