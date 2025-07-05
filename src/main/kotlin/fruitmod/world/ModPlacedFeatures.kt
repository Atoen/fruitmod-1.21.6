package fruitmod.world

import fruitmod.FruitMod
import fruitmod.block.ModBlocks
import net.minecraft.registry.Registerable
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.feature.PlacedFeature
import net.minecraft.world.gen.feature.PlacedFeatures
import net.minecraft.world.gen.feature.VegetationPlacedFeatures
import net.minecraft.world.gen.placementmodifier.PlacementModifier

object ModPlacedFeatures {

    val DRIFTWOOD_PLACED_KEY = registerKey("driftwood_placed")

    fun bootstrap(context: Registerable<PlacedFeature>) {
        val configuredFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)

        register(context, DRIFTWOOD_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DRIFTWOOD_KEY),
            VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                PlacedFeatures.createCountExtraModifier(2, 0.1f, 2), ModBlocks.DRIFTWOOD_SAPLING
            ))
    }

    fun registerKey(name: String): RegistryKey<PlacedFeature> {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(FruitMod.MOD_ID, name))
    }

    private fun register(
        context: Registerable<PlacedFeature>,
        key: RegistryKey<PlacedFeature>,
        configuration: RegistryEntry<ConfiguredFeature<*, *>>,
        modifiers: List<PlacementModifier>
    ) {
        context.register(key, PlacedFeature(configuration, modifiers))
    }

    private fun <FC : FeatureConfig, F : Feature<FC>> register(
        context: Registerable<PlacedFeature>,
        key: RegistryKey<PlacedFeature>,
        configuration: RegistryEntry<ConfiguredFeature<*, *>>,
        vararg modifier: PlacementModifier
    ) {
        register(context, key, configuration, modifier.asList())
    }
}