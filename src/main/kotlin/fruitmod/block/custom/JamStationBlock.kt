package fruitmod.block.custom

import com.mojang.serialization.MapCodec
import fruitmod.ModTags
import fruitmod.block.entity.ModBlockEntities
import fruitmod.block.entity.custom.JamStationBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldView
import net.minecraft.world.tick.ScheduledTickView

class JamStationBlock(settings: Settings) : BlockWithEntity(settings) {

    init {
        defaultState = stateManager.defaultState
            .with(HEATED, false)
            .with(HAS_JAR, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(HEATED, HAS_JAR)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState) =
        JamStationBlockEntity(pos, state)

    override fun getStateForNeighborUpdate(
        state: BlockState,
        world: WorldView,
        tickView: ScheduledTickView,
        pos: BlockPos,
        direction: Direction,
        neighborPos: BlockPos,
        neighborState: BlockState,
        random: Random
    ): BlockState {
        if (direction != Direction.DOWN) {
            return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random)
        }

        val isHeated = neighborState.isIn(ModTags.Blocks.HEAT_SOURCES)
        return state.with(HEATED, isHeated)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        if (world.isClient) {
            return ActionResult.SUCCESS
        }

        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is JamStationBlockEntity) {
            player.openHandledScreen(blockEntity)
        }

        return ActionResult.SUCCESS
    }

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return if (!world.isClient) {
            validateTicker(type, ModBlockEntities.JAM_STATION, JamStationBlockEntity::tick)
        } else {
            null
        }
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = SHAPE

    override fun onStateReplaced(state: BlockState, world: ServerWorld, pos: BlockPos, moved: Boolean) {
        ItemScatterer.onStateReplaced(state, world, pos)
    }

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) =
        ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))

    override fun canPathfindThrough(state: BlockState, type: NavigationType) = false

    override fun getCodec() = CODEC

    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL

    companion object {
        private val SHAPE = createCuboidShape(0.0, 0.0, 0.0, 16.0, 7.0, 16.0)
        val CODEC: MapCodec<JamStationBlock> = createCodec(::JamStationBlock)

        val HEATED = BooleanProperty.of("heated")
        val HAS_JAR = BooleanProperty.of("has_jar")
    }
}