package fruitmod.world.gen

import fruitmod.world.ModPlacedFeatures
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.world.biome.BiomeKeys
import net.minecraft.world.gen.GenerationStep

object ModTreeGeneration {
    fun generateTrees() {
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(
            BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU
        ), GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.DRIFTWOOD_PLACED_KEY)
    }
}