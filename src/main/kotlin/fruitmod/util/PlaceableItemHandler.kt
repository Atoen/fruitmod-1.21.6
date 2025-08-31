package fruitmod.util

import fruitmod.block.ModBlocks
import fruitmod.item.ModItems
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.Items
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World

object PlaceableItemHandler {
    private val itemMap: MutableMap<Item, Block> = mutableMapOf()

    fun registerPlaceableItem(item: Item, block: Block) {
        itemMap[item] = block
    }

    fun setupDefault() {
        registerPlaceableItem(Items.APPLE, ModBlocks.APPLE_BLOCK)
        registerPlaceableItem(Items.GOLDEN_APPLE, ModBlocks.GOLDEN_APPLE_BLOCK)
        registerPlaceableItem(Items.ENCHANTED_GOLDEN_APPLE, ModBlocks.ENCHANTED_GOLDEN_APPLE_BLOCK)
        registerPlaceableItem(ModItems.ORANGE, ModBlocks.ORANGE_BLOCK)

        init()
    }

    fun init() {
        UseBlockCallback.EVENT.register(::callback)
    }

    private fun callback(
        player: PlayerEntity,
        world: World,
        hand: Hand,
        hitResult: BlockHitResult
    ): ActionResult {
        if (player.isSpectator) {
            return ActionResult.PASS
        }

        val stack = player.getStackInHand(hand)
        if (stack.isEmpty || !player.isSneaking) {
            return ActionResult.PASS
        }

        val block = itemMap[stack.item] ?: return ActionResult.PASS
        val pos = hitResult.blockPos.offset(hitResult.side)

        if (!world.canEntityModifyAt(player, pos)) {
            return ActionResult.PASS
        }

        val context = ItemPlacementContext(player, hand, stack, hitResult)
        val state = block.getPlacementState(context) ?: return ActionResult.FAIL

        if (world.isClient) {
            return ActionResult.SUCCESS
        }

        if (world.setBlockState(pos, state)) {
            stack.decrementUnlessCreative(1, player)
            val soundGroup = state.soundGroup
            world.playSound(
                null, pos,
                soundGroup.placeSound,
                SoundCategory.BLOCKS,
                (soundGroup.volume + 1f) / 2f,
                soundGroup.pitch * 0.8f
            )
        }

        return ActionResult.SUCCESS
    }
}

