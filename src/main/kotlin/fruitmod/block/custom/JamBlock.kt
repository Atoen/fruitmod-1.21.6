package fruitmod.block.custom

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityCollisionHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class JamBlock(settings: Settings) : BaseJamBlock(settings) {

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
    ) = entity.slowMovement(state, MOVEMENT_MULTIPLIER)

    override fun getCameraCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext?
    ): VoxelShape = VoxelShapes.empty()

    override fun getInsideCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        entity: Entity
    ): VoxelShape = VoxelShapes.fullCube()

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = VoxelShapes.empty()

    override fun getCodec() = CODEC

    companion object {
        val CODEC: MapCodec<JamBlock> = createCodec(::JamBlock)
        val MOVEMENT_MULTIPLIER = Vec3d(0.5, 0.25, 0.5)
    }
}