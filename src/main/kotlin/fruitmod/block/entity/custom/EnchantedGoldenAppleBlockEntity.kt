package fruitmod.block.entity.custom

import fruitmod.block.entity.ModBlockEntities
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class EnchantedGoldenAppleBlockEntity(
    pos: BlockPos,
    state: BlockState
) : BlockEntity(ModBlockEntities.ENCHANTED_GOLDEN_APPLE, pos, state)
