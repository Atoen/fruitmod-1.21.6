package fruitmod.block.custom

import fruitmod.item.ModItems
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.TintedParticleLeavesBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class DriftwoodLeavesBlock(settings: Settings) : TintedParticleLeavesBlock(0.1f, settings) {
    companion object {
        val HAS_FRUIT: BooleanProperty = BooleanProperty.of("has_fruit")
    }

    init {
        defaultState = this.stateManager.defaultState.with(HAS_FRUIT, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(HAS_FRUIT)
    }

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        val hasFruit = state.get(HAS_FRUIT)
        val distance = state.get(DISTANCE)

        if (distance == 7) {
            if (hasFruit) {
                dropStack(world, pos, ItemStack(ModItems.ORANGE))
            }
            if (shouldDecay(state)) {
                world.removeBlock(pos, false)
            }
        } else if (distance < 7 && !hasFruit && random.nextFloat() < 0.5f) {
            world.setBlockState(pos, state.with(HAS_FRUIT, true), 3)
        }
    }

    override fun hasRandomTicks(state: BlockState): Boolean {
        return !state.get(HAS_FRUIT) || state.get(DISTANCE) == 7
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        if (!state.get(HAS_FRUIT)) {
            return ActionResult.PASS
        }

        if (world.isClient) {
            return ActionResult.SUCCESS
        }

        val itemStack = ItemStack(ModItems.ORANGE)
        dropStack(world, pos, itemStack)

        world.setBlockState(pos, state.with(HAS_FRUIT, false), 2)

        world.playSound(
            null as Entity?,
            pos,
            SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES,
            SoundCategory.BLOCKS,
            1.0f,
            0.8f + world.random.nextFloat() * 0.4f
        )

        return ActionResult.SUCCESS
    }
}