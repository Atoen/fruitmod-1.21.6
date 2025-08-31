package fruitmod.block.custom

import com.mojang.serialization.MapCodec
import fruitmod.block.entity.custom.PedestalBlockEntity
import fruitmod.util.isServer
import net.minecraft.block.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class PedestalBlock(
    settings: Settings
) : BlockWithEntity(settings), BlockEntityProvider {

    override fun onUseWithItem(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {

        val blockEntity = world.getBlockEntity(pos) as? PedestalBlockEntity
        if (blockEntity == null) {
            return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION
        }

        if (player.isSneaking && world.isServer) {
            player.openHandledScreen(blockEntity)
        }

        return ActionResult.SUCCESS
    }

    override fun createBlockEntity(
        pos: BlockPos,
        state: BlockState,
    ) = PedestalBlockEntity(pos, state)

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = SHAPE

    override fun getCodec() = CODEC

    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL

    companion object {
        private val SHAPE = createCuboidShape(2.0, 0.0, 2.0, 14.0, 13.0, 14.0)
        val CODEC: MapCodec<PedestalBlock> = createCodec(::PedestalBlock)
    }
}