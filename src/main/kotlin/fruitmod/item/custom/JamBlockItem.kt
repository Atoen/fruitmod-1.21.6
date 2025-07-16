package fruitmod.item.custom

import fruitmod.component.ModDataComponents
import net.minecraft.block.Block
import net.minecraft.component.type.TooltipDisplayComponent
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.function.Consumer

abstract class BaseJamBlockItem(
    block: Block,
    settings: Settings,
    val nameKey: String,
    val templatedNameKey: String,
) : BlockItem(block, settings) {

    override fun getName(stack: ItemStack): Text {
        val color = stack.get(ModDataComponents.JAM_BLOCK_COLOR) ?:
            return Text.translatable(nameKey)

        val scaled = color.channels.toFullColorSpace().toInt()
        return Text.translatable(templatedNameKey, Text.literal("●").withColor(scaled))
    }

    @Deprecated("Deprecated in Java")
    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        displayComponent: TooltipDisplayComponent,
        textConsumer: Consumer<Text>,
        type: TooltipType
    ) {
        val color = stack.get(ModDataComponents.JAM_BLOCK_COLOR) ?: return

        val (r, g, b) = color

        val rText = Text.translatable("jam.fruitmod.rgb_R").formatted(Formatting.RED)
        val gText = Text.translatable("jam.fruitmod.rgb_g").formatted(Formatting.GREEN)
        val bText = Text.translatable("jam.fruitmod.rgb_b").formatted(Formatting.BLUE)
        val gray = Formatting.GRAY

        val rgbText = Text.literal("")
            .append(rText).append(Text.literal(": $r, ").formatted(gray))
            .append(gText).append(Text.literal(": $g, ").formatted(gray))
            .append(bText).append(Text.literal(": $b").formatted(gray))

        val hex = String.format("#%02X%02X%02X", r * 17, g * 17, b * 17)
        val hexText = Text.literal("Hex: $hex").formatted(Formatting.DARK_GRAY)

        textConsumer.accept(rgbText)
        textConsumer.accept(hexText)
    }
}

class JamBlockItem(block: Block, settings: Settings) : BaseJamBlockItem(
    block, settings, "item.fruitmod.jam_block_item", "item.fruitmod.jam_block_item_templated"
)

class SolidJamBlockItem(block: Block, settings: Settings) : BaseJamBlockItem(
    block, settings, "item.fruitmod.solid_jam_block_item", "item.fruitmod.solid_jam_block_item_templated"
)
