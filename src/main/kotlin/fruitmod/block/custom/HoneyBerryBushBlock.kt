package fruitmod.block.custom

import fruitmod.item.ModItems
import net.minecraft.block.BlockState
import net.minecraft.block.SweetBerryBushBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldView
import net.minecraft.world.event.GameEvent

class HoneyBerryBushBlock(settings: Settings) : SweetBerryBushBlock(settings) {

    companion object {
        val AGE = Properties.AGE_3
    }

    init {
        defaultState = this.stateManager.defaultState.with(AGE, 0)
    }

    override fun getPickStack(world: WorldView, pos: BlockPos, state: BlockState, includeData: Boolean): ItemStack {
        return ItemStack(ModItems.HONEY_BERRIES)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        val age = state.get(AGE) as Int
        val isMaxAge = age == 3

        if (age <= 1) return ActionResult.PASS

        val bonusDropAmount = 1 + world.random.nextInt(50)
        dropStack(world, pos, ItemStack(ModItems.HONEY_BERRIES, bonusDropAmount + (if (isMaxAge) 1 else 0)))

        world.playSound(
            null as Entity?,
            pos,
            SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES,
            SoundCategory.BLOCKS,
            1.0f,
            0.8f + world.random.nextFloat() * 0.4f
        )

        val blockState = state.with(AGE, 1)

        world.setBlockState(pos, blockState, 2)
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, blockState))

        return ActionResult.SUCCESS
    }

    override fun canPathfindThrough(state: BlockState, type: NavigationType): Boolean {
        return false
    }
}