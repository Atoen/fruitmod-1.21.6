package fruitmod.entity

import fruitmod.item.ModItems
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

class CoconutEntity : ThrownItemEntity {

    constructor(
        type: EntityType<out ThrownItemEntity>,
        x: Double,
        y: Double,
        z: Double,
        world: World,
        stack: ItemStack
    ) : super(type, x, y, z, world, stack)

    constructor(
        world: World,
        owner: LivingEntity,
        stack: ItemStack
    ) : super(EntityType.EGG, owner, world, stack)

    constructor(
        type: EntityType<out ThrownItemEntity>,
        world: World
    ) : super(type, world)

    override fun handleStatus(status: Byte) {
        if (status.toInt() == 3) {
            repeat(8) {
                this.world.addParticleClient(
                    ItemStackParticleEffect(ParticleTypes.ITEM, this.stack),
                    this.x,
                    this.y,
                    this.z,
                    (this.random.nextFloat().toDouble() - 0.5) * 0.08,
                    (this.random.nextFloat().toDouble() - 0.5) * 0.08,
                    (this.random.nextFloat().toDouble() - 0.5) * 0.08
                )
            }
        }
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)

        val world = entityHitResult.entity.world
        if (world is ServerWorld) {
            entityHitResult.entity.damage(world, damageSources.thrown(this, this.getOwner()), 5f)
        }
    }

    override fun onCollision(hitResult: HitResult?) {
        super.onCollision(hitResult)

        if (!world.isClient) {
            world.sendEntityStatus(this, 3.toByte())
            discard()
        }
    }

    override fun getDefaultItem() = ModItems.COCONUT
}