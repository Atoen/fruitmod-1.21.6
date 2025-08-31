package fruitmod.item.custom

import fruitmod.entity.CoconutEntity
import fruitmod.entity.ModEntities
import fruitmod.sound.ModSounds
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ProjectileItem
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.world.World

class CoconutItem(
    settings: Settings
) : Item(settings), ProjectileItem {

    override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
        val itemStack = user.getStackInHand(hand)
        world.playSound(
            null as Entity?,
            user.x,
            user.y,
            user.z,
            ModSounds.COCONUT_THROW,
            SoundCategory.PLAYERS,
            0.5f,
            0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
        )

        if (world is ServerWorld) {
            ProjectileEntity.spawnWithVelocity(::CoconutEntity, world, itemStack, user, 0f, 0.8f, 1f)
        }

        itemStack.decrementUnlessCreative(1, user)

        return ActionResult.SUCCESS
    }

    override fun createEntity(
        world: World,
        pos: Position,
        stack: ItemStack,
        direction: Direction
    ): ProjectileEntity {
        return CoconutEntity(ModEntities.COCONUT, pos.x, pos.y, pos.z, world, stack)
    }
}