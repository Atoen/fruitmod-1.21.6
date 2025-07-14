package fruitmod.block.custom

import com.mojang.serialization.MapCodec
import fruitmod.block.JamBlockProperties
import fruitmod.component.JamBlockColorComponent
import fruitmod.component.ModDataComponents
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView

class SolidJamBlock(settings: Settings) : Block(settings) {

    init {
        defaultState = stateManager.defaultState
            .with(RED, 13)
            .with(GREEN, 2)
            .with(BLUE, 4)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(JamBlock.Companion.RED, JamBlock.Companion.GREEN, JamBlock.Companion.BLUE)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val component = ctx.stack.get(ModDataComponents.JAM_BLOCK_COLOR) ?: return defaultState

        return defaultState
            .with(JamBlockProperties.RED, component.red)
            .with(JamBlockProperties.GREEN, component.green)
            .with(JamBlockProperties.BLUE, component.blue)
    }

    override fun getPickStack(world: WorldView, pos: BlockPos, state: BlockState, includeData: Boolean): ItemStack {
        val stack = ItemStack(this)

        val r = state.get(JamBlockProperties.RED)
        val g = state.get(JamBlockProperties.GREEN)
        val b = state.get(JamBlockProperties.BLUE)

        stack.set(ModDataComponents.JAM_BLOCK_COLOR, JamBlockColorComponent(r, g, b))
        return stack
    }

    override fun getCodec() = JamBlock.Companion.CODEC

    companion object {
        val RED = JamBlockProperties.RED
        val GREEN = JamBlockProperties.GREEN
        val BLUE = JamBlockProperties.BLUE

        val CODEC: MapCodec<SolidJamBlock> = createCodec(::SolidJamBlock)
    }
}