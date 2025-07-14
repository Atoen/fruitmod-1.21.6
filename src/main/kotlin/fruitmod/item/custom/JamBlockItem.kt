package fruitmod.item.custom

import fruitmod.component.ModDataComponents
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class JamBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {

    override fun getName(stack: ItemStack): Text {
        val component = stack.get(ModDataComponents.JAM_BLOCK_COLOR)
        return if (component != null) {
            val (r, g, b) = component
            Text.literal("Jam Block (R: $r, G: $g, B: $b)")
        } else {
            super.getName(stack)
        }
    }
}