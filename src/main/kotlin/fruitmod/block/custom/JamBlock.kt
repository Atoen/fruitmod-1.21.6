package fruitmod.block.custom

import com.mojang.serialization.MapCodec
import fruitmod.block.JamBlockProperties
import fruitmod.component.JamBlockColorComponent
import fruitmod.component.ModDataComponents
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.EntityShapeContext
import net.minecraft.block.ShapeContext
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityCollisionHandler
import net.minecraft.entity.FallingBlockEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldView

class JamBlock(settings: Settings) : Block(settings) {

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

    override fun isSideInvisible(
        state: BlockState,
        stateFrom: BlockState,
        direction: Direction
    ) = stateFrom.isOf(this)

    override fun onEntityCollision(
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: Entity,
        handler: EntityCollisionHandler
    ) {
        entity.slowMovement(state, MOVEMENT_MULTIPLIER)
    }

    override fun getCameraCollisionShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape? {
        return VoxelShapes.empty()
    }

    override fun getInsideCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        entity: Entity
    ): VoxelShape {
        val shape = getCollisionShape(state, world, pos, ShapeContext.of(entity))
        return if (shape.isEmpty) VoxelShapes.fullCube() else shape
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        if (!context.isPlacement && context is EntityShapeContext) {
            val entity = context.entity
            if (entity != null && entity.fallDistance > 2.5) {
                return FALLING_SHAPE
            }

            val isFallingBlock = entity is FallingBlockEntity
            if (isFallingBlock && context.isAbove(VoxelShapes.fullCube(), pos, false) && !context.isDescending) {
                return super.getCollisionShape(state, world, pos, context)
            }
        }

        return VoxelShapes.empty()
    }

    override fun getCodec() = CODEC

    companion object {
        val RED = JamBlockProperties.RED
        val GREEN = JamBlockProperties.GREEN
        val BLUE = JamBlockProperties.BLUE

        val CODEC: MapCodec<JamBlock> = createCodec(::JamBlock)
        val MOVEMENT_MULTIPLIER = Vec3d(0.5, 0.5, 0.5)

        private val FALLING_SHAPE = VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.9, 1.0)
    }
}