package fruitmod.world

import fruitmod.FruitMod
import fruitmod.block.ModBlocks
import net.minecraft.block.LeavesBlock
import net.minecraft.registry.Registerable
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.util.math.intprovider.ConstantIntProvider
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize
import net.minecraft.world.gen.foliage.BlobFoliagePlacer
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.trunk.StraightTrunkPlacer

object ModConfiguredFeatures {

    val DRIFTWOOD_KEY = registerKey("driftwood")

    fun bootstrap(context: Registerable<ConfiguredFeature<*, *>>) {

        val leavesProvider = BlockStateProvider.of(ModBlocks.DRIFTWOOD_LEAVES.defaultState.with(
            LeavesBlock.PERSISTENT, false))


        register(context, DRIFTWOOD_KEY, Feature.TREE, TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.DRIFTWOOD_LOG),
            StraightTrunkPlacer(2, 1, 1),
            leavesProvider,
            BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(1), 3),
            TwoLayersFeatureSize(1, 0, 1)
        ).build())
    }

    fun registerKey(name: String): RegistryKey<ConfiguredFeature<*, *>> {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(FruitMod.MOD_ID, name))
    }

    private fun <FC : FeatureConfig, F : Feature<FC>> register(
        context: Registerable<ConfiguredFeature<*, *>>,
        key: RegistryKey<ConfiguredFeature<*, *>>,
        feature: F,
        featureConfiguration: FC
    ) {
        context.register(key, ConfiguredFeature(feature, featureConfiguration))
    }
}