package fruitmod.particle

import fruitmod.FruitMod
import fruitmod.util.modIdentifier
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.SimpleParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModParticles {

    val BONK_PARTICLE = registerParticle("bonk_particle", FabricParticleTypes.simple(true))

    fun registerParticles() {
        FruitMod.logger.info("Registering particles for {}", FruitMod.MOD_ID)
    }

    private fun registerParticle(name: String, particleType: SimpleParticleType): SimpleParticleType {
        return Registry.register(Registries.PARTICLE_TYPE, modIdentifier(name), particleType)
    }
}