package fruitmod

import fruitmod.block.ModBlocks
import fruitmod.item.property.JamPortionsProperty
import fruitmod.particle.BonkParticle
import fruitmod.particle.ModParticles
import fruitmod.tint.JamTintSource
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap
import net.minecraft.client.render.BlockRenderLayer
import net.minecraft.client.render.item.property.select.SelectProperties
import net.minecraft.client.render.item.tint.TintSourceTypes
import net.minecraft.util.Identifier

object FruitModClient : ClientModInitializer {
	override fun onInitializeClient() {
		BlockRenderLayerMap.putBlock(ModBlocks.HONEY_BERRY_BUSH, BlockRenderLayer.CUTOUT)
		BlockRenderLayerMap.putBlock(ModBlocks.DRIFTWOOD_SAPLING, BlockRenderLayer.CUTOUT)

		ParticleFactoryRegistry.getInstance().register(ModParticles.BONK_PARTICLE) {
			BonkParticle.Factory(it)
		}

		SelectProperties.ID_MAPPER.put(
			Identifier.of(FruitMod.MOD_ID, "jam_portions"),
			JamPortionsProperty.TYPE
		)

		TintSourceTypes.ID_MAPPER.put(
			Identifier.of(FruitMod.MOD_ID, "jam"),
			JamTintSource.CODEC
		)
    }
}