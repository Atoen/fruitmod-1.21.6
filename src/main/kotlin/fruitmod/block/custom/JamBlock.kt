package fruitmod.block.custom

import com.mojang.serialization.MapCodec
import fruitmod.block.ModBlocks
import fruitmod.ModTags
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.CandleBlock
import net.minecraft.block.ShapeContext
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityCollisionHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class JamBlock(settings: Settings) : BaseJamBlock(settings) {

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        if (!isNearHeatSource(world, pos)) return

        val r = state.get(RED)
        val g = state.get(GREEN)
        val b = state.get(BLUE)

        val newState = ModBlocks.SOLID_JAM_BLOCK.defaultState
            .with(RED, r)
            .with(GREEN, g)
            .with(BLUE, b)

        world.setBlockState(pos, newState)
        world.playSound(null, pos, SoundEvents.BLOCK_HONEY_BLOCK_STEP, SoundCategory.BLOCKS, 1f, 0.8f)
    }

    private fun isNearHeatSource(world: World, pos: BlockPos): Boolean {
        if (world.registryKey == World.NETHER) {
            return true
        }

        val mutablePos = BlockPos.Mutable()
        HEAT_OFFSETS.forEach {
            mutablePos.set(pos.x + it.x, pos.y + it.y, pos.z + it.z)
            val state = world.getBlockState(mutablePos)
            val block = state.block

            if (state.isIn(ModTags.Blocks.HEAT_SOURCES)) {
                return true
            }

            if ((block == Blocks.FURNACE || block == Blocks.SMOKER || block == Blocks.BLAST_FURNACE) &&
                state.getOrEmpty(Properties.LIT).orElse(false)
            ) {
                return true
            }

            if ((block == Blocks.CAMPFIRE || block == Blocks.SOUL_CAMPFIRE) &&
                state.getOrEmpty(Properties.LIT).orElse(false)
            ) {
                return true
            }

            if (block is CandleBlock && state.getOrEmpty(Properties.LIT).orElse(false)) {
                return true
            }
        }

        return false
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

        private val HEAT_OFFSETS: Array<BlockPos> = buildList {
            for (dx in -1..1) {
                for (dy in -1..1) {
                    for (dz in -1..1) {
                        if (dx != 0 || dy != 0 || dz != 0) {
                            add(BlockPos(dx, dy, dz))
                        }
                    }
                }
            }
        }.toTypedArray()
    }
}