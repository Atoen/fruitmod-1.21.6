package fruitmod.world

import fruitmod.block.ModBlocks
import fruitmod.util.modIdentifier
import net.minecraft.registry.Registerable
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.world.gen.feature.*
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
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, modIdentifier(name))
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