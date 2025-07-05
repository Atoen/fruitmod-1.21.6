package fruitmod.particle

import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.particle.SpriteBillboardParticle
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.SimpleParticleType

class BonkParticle(
    clientWorld: ClientWorld, x: Double, y: Double, z: Double,
    spriteProvider: SpriteProvider,
    velocityX: Double, velocityY: Double, velocityZ: Double
) : SpriteBillboardParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ) {

    init {
        maxAge = 40
        setSpriteForAge(spriteProvider)
        scale = 0.5f
        gravityStrength = 0.0f
        alpha = 1.0f     // fully opaque at start
    }

    override fun tick() {
        super.tick()

        // Make it rise slowly
        velocityY = 0.01

        // Fade out
        alpha = 1.0f - (age.toFloat() / maxAge.toFloat())

        // Optional: slow down horizontal motion
        velocityX *= 0.95
        velocityZ *= 0.95
    }


    override fun getType() = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT!!

    class Factory(private val provider: SpriteProvider) : ParticleFactory<SimpleParticleType> {
        override fun createParticle(
            parameters: SimpleParticleType,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            return BonkParticle(world, x, y, z, provider, velocityX, velocityY, velocityZ)
        }
    }
}