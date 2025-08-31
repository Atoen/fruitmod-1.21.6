package fruitmod.block.custom

import fruitmod.util.isServer
import net.minecraft.block.*
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.state.StateManager
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldView

abstract class PlacedFruitBlock(
    settings: Settings,
    val fruitItem: Item,
    val shape: VoxelShape,
) : HorizontalFacingBlock(settings) {

    init {
        defaultState = stateManager.defaultState
            .with(FACING, Direction.NORTH)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val stackInHand = player.getStackInHand(player.activeHand)
        if (!stackInHand.isEmpty && !stackInHand.isOf(fruitItem)) {
            return ActionResult.PASS
        }

        if (!world.isServer) {
            return ActionResult.SUCCESS
        }

        world.removeBlock(pos, false)

        val stack = fruitItem.defaultStack
        if (!player.inventory.insertStack(stack)) {
            val droppedItemEntity = ItemEntity(world, pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, stack)
            world.spawnEntity(droppedItemEntity)
        }

        val soundGroup = state.soundGroup
        world.playSound(
            null, pos,
            soundGroup.breakSound,
            SoundCategory.BLOCKS,
            (soundGroup.volume + 1f) / 2f,
            soundGroup.pitch * 0.8f
        )

        return ActionResult.SUCCESS
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val horizontalFacing = ctx.horizontalPlayerFacing.opposite
        val state =  defaultState.with(FACING, horizontalFacing)

        return if (canPlace(ctx, state)) state else null
    }

    override fun getPickStack(world: WorldView, pos: BlockPos, state: BlockState, includeData: Boolean): ItemStack {
        return ItemStack(fruitItem)
    }

    private fun canPlace(ctx: ItemPlacementContext, state: BlockState): Boolean {
        val player = ctx.player
        val world = ctx.world

        return state.canPlaceAt(world, ctx.blockPos) && world.canPlace(state, ctx.blockPos, ShapeContext.ofPlacement(player))
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ) = shape

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ) = shape

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING)
    }
}
