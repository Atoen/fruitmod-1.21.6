package fruitmod.block.custom

import fruitmod.block.JamBlockProperties
import fruitmod.component.JamBlockColorComponent
import fruitmod.component.ModDataComponents
import fruitmod.util.JamColorHelper
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.DyeItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.loot.context.LootWorldContext
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldView

abstract class BaseJamBlock(settings: Settings) : Block(settings) {

    init {
        defaultState = stateManager.defaultState
            .with(RED, 13)
            .with(GREEN, 2)
            .with(BLUE, 4)
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

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(RED, GREEN, BLUE)
    }

    override fun getDroppedStacks(state: BlockState, builder: LootWorldContext.Builder): List<ItemStack> {
        val stack= ItemStack(this).apply {
            val r = state.get(JamBlockProperties.RED)
            val g = state.get(JamBlockProperties.GREEN)
            val b = state.get(JamBlockProperties.BLUE)

            set(ModDataComponents.JAM_BLOCK_COLOR, JamBlockColorComponent(r, g, b))
        }

        return listOf(stack)
    }

    override fun onUseWithItem(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        val item = stack.item
        if (item !is DyeItem) {
            return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION
        }

        val (r, g, b) = JamColorHelper.mapDyeColor(item.color)
        val isDifferent = state.run {
            r != get(RED) || g != get(GREEN) || b != get(BLUE)
        }

        if (!isDifferent) {
            return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION
        }

        if (!world.isClient) {
            stack.decrementUnlessCreative(1, player)

            val newState = state
                .with(RED, r)
                .with(GREEN, g)
                .with(BLUE, b)

            world.setBlockState(pos, newState)
            world.playSound(null, pos, SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1f, 1f)
        }

        return ActionResult.SUCCESS
    }

    companion object {
        val RED = JamBlockProperties.RED
        val GREEN = JamBlockProperties.GREEN
        val BLUE = JamBlockProperties.BLUE
    }
}