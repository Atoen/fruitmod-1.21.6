package fruitmod

import fruitmod.block.JamBlockProperties
import fruitmod.block.ModBlocks
import fruitmod.item.property.JamPortionsProperty
import fruitmod.particle.BonkParticle
import fruitmod.particle.ModParticles
import fruitmod.tint.JamBlockItemTintSource
import fruitmod.tint.JamTintSource
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.render.BlockRenderLayer
import net.minecraft.client.render.item.property.select.SelectProperties
import net.minecraft.client.render.item.tint.TintSourceTypes
import net.minecraft.util.Identifier
import net.minecraft.util.math.ColorHelper

object FruitModClient : ClientModInitializer {
	override fun onInitializeClient() {
		BlockRenderLayerMap.putBlock(ModBlocks.HONEY_BERRY_BUSH, BlockRenderLayer.CUTOUT)
		BlockRenderLayerMap.putBlock(ModBlocks.DRIFTWOOD_SAPLING, BlockRenderLayer.CUTOUT)
		BlockRenderLayerMap.putBlock(ModBlocks.JAM_BLOCK, BlockRenderLayer.TRANSLUCENT)

		ParticleFactoryRegistry.getInstance().register(ModParticles.BONK_PARTICLE) {
			BonkParticle.Factory(it)
		}

		SelectProperties.ID_MAPPER.put(
			Identifier.of(FruitMod.MOD_ID, "jam_portions"),
			JamPortionsProperty.TYPE
		)

		TintSourceTypes.ID_MAPPER.run {
			put(Identifier.of(FruitMod.MOD_ID, "jam"), JamTintSource.CODEC)
			put(Identifier.of(FruitMod.MOD_ID, "jam_block_item"), JamBlockItemTintSource.CODEC)
		}

		ColorProviderRegistry.BLOCK.register({ state, _, _, _ ->
			val r = state.get(JamBlockProperties.RED) * 17
			val g = state.get(JamBlockProperties.GREEN) * 17
			val b = state.get(JamBlockProperties.BLUE) * 17
			ColorHelper.getArgb(r, g, b)
		}, ModBlocks.JAM_BLOCK, ModBlocks.SOLID_JAM_BLOCK)
    }
}