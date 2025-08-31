package fruitmod.block.custom

import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape

abstract class PlacedFruitBlockWithEntity(
    settings: Settings,
    fruitItem: Item,
    shape: VoxelShape,
    private val blockEntityFactory: (BlockPos, BlockState) -> BlockEntity
) : PlacedFruitBlock(settings, fruitItem, shape), BlockEntityProvider {

    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return blockEntityFactory(pos, state)
    }
}
