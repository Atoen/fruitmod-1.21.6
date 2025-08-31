package fruitmod

import fruitmod.block.JamBlockProperties
import fruitmod.block.ModBlocks
import fruitmod.block.entity.ModBlockEntities
import fruitmod.entity.ModEntities
import fruitmod.gui.JamStationScreen
import fruitmod.gui.PedestalScreen
import fruitmod.item.property.JamPortionsProperty
import fruitmod.particle.BonkParticle
import fruitmod.particle.ModParticles
import fruitmod.render.EnchantedGoldenAppleRenderer
import fruitmod.screen.ModScreenHandlers
import fruitmod.tint.JamBlockItemTintSource
import fruitmod.tint.JamTintSource
import fruitmod.util.modIdentifier
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.render.BlockRenderLayer
import net.minecraft.client.render.entity.EntityRenderers
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.client.render.item.property.select.SelectProperties
import net.minecraft.client.render.item.tint.TintSourceTypes
import net.minecraft.util.math.ColorHelper

object FruitModClient : ClientModInitializer {
	override fun onInitializeClient() {
		BlockRenderLayerMap.putBlock(ModBlocks.HONEY_BERRY_BUSH, BlockRenderLayer.CUTOUT)
		BlockRenderLayerMap.putBlock(ModBlocks.DRIFTWOOD_SAPLING, BlockRenderLayer.CUTOUT)
		BlockRenderLayerMap.putBlock(ModBlocks.JAM_BLOCK, BlockRenderLayer.TRANSLUCENT)

		BlockEntityRendererRegistryImpl.register(ModBlockEntities.ENCHANTED_GOLDEN_APPLE, ::EnchantedGoldenAppleRenderer)
		EntityRendererRegistry.register(ModEntities.COCONUT, ::FlyingItemEntityRenderer)

		ParticleFactoryRegistry.getInstance().register(ModParticles.BONK_PARTICLE) {
			BonkParticle.Factory(it)
		}

		SelectProperties.ID_MAPPER.put(
			modIdentifier("jam_portions"),
			JamPortionsProperty.TYPE
		)

		TintSourceTypes.ID_MAPPER.run {
			put(modIdentifier("jam"), JamTintSource.CODEC)
			put(modIdentifier("jam_block_item"), JamBlockItemTintSource.CODEC)
		}

		ColorProviderRegistry.BLOCK.register({ state, _, _, _ ->
			val r = state.get(JamBlockProperties.RED) * 17
			val g = state.get(JamBlockProperties.GREEN) * 17
			val b = state.get(JamBlockProperties.BLUE) * 17
			ColorHelper.getArgb(r, g, b)
		}, ModBlocks.JAM_BLOCK, ModBlocks.SOLID_JAM_BLOCK)

		HandledScreens.register(ModScreenHandlers.PEDESTAL_SCREEN_HANDLER, ::PedestalScreen)
		HandledScreens.register(ModScreenHandlers.JAM_STATION_SCREEN_HANDLER, ::JamStationScreen)
    }
}