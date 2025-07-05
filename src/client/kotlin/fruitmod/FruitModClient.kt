package fruitmod

import fruitmod.block.ModBlocks
import fruitmod.particle.BonkParticle
import fruitmod.particle.ModParticles
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap
import net.minecraft.client.render.BlockRenderLayer

object FruitModClient : ClientModInitializer {
	override fun onInitializeClient() {
		BlockRenderLayerMap.putBlock(ModBlocks.HONEY_BERRY_BUSH, BlockRenderLayer.CUTOUT)
		BlockRenderLayerMap.putBlock(ModBlocks.DRIFTWOOD_SAPLING, BlockRenderLayer.CUTOUT)

		ParticleFactoryRegistry.getInstance().register(ModParticles.BONK_PARTICLE) { BonkParticle.Factory(it) }
    }
}