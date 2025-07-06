package fruitmod.datagen

import fruitmod.block.ModBlocks
import fruitmod.block.custom.DriftwoodLeavesBlock
import fruitmod.item.ModItems
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.client.data.BlockStateModelGenerator
import net.minecraft.client.data.ItemModelGenerator
import net.minecraft.client.data.Models
import net.minecraft.client.data.TextureMap
import net.minecraft.client.data.TexturedModel
import net.minecraft.client.data.VariantsBlockModelDefinitionCreator
import net.minecraft.client.render.model.json.ModelVariant
import net.minecraft.client.render.model.json.WeightedVariant
import net.minecraft.util.collection.Pool
import net.minecraft.item.Item

class ModModelGenerator(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        generator.registerSimpleCubeAll(ModBlocks.COMPACTED_DIRT)

        generator.createLogTexturePool(ModBlocks.DRIFTWOOD_LOG)
            .log(ModBlocks.DRIFTWOOD_LOG)
            .wood(ModBlocks.DRIFTWOOD_WOOD)

        generator.createLogTexturePool(ModBlocks.STRIPPED_DRIFTWOOD_LOG)
            .log(ModBlocks.STRIPPED_DRIFTWOOD_LOG)
            .wood(ModBlocks.STRIPPED_DRIFTWOOD_WOOD)

        generator.registerSimpleCubeAll(ModBlocks.DRIFTWOOD_PLANKS)
        generator.registerTintableCross(ModBlocks.DRIFTWOOD_SAPLING, BlockStateModelGenerator.CrossType.NOT_TINTED)

        val baseModel = TexturedModel.LEAVES.upload(ModBlocks.DRIFTWOOD_LEAVES, generator.modelCollector)
        val fruitModel = generator.createSubModel(ModBlocks.DRIFTWOOD_LEAVES, "_fruit", Models.LEAVES, TextureMap::all)
        generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(ModBlocks.DRIFTWOOD_LEAVES)
            .with(BlockStateModelGenerator.createBooleanModelMap(DriftwoodLeavesBlock.HAS_FRUIT,
                WeightedVariant(Pool.builder<ModelVariant>().add(ModelVariant(fruitModel)).build()),
                WeightedVariant(Pool.builder<ModelVariant>().add(ModelVariant(baseModel)).build()
                ))))
    }

    override fun generateItemModels(generator: ItemModelGenerator) {
        generator.registerGenerated(
            ModItems.BANANA,
            ModItems.BLUEBERRY,
            ModItems.CHERRIES,
            ModItems.COCONUT,
            ModItems.OPEN_COCONUT,
            ModItems.GRAPES,
            ModItems.HONEY_BERRIES,
            ModItems.KIWI,
            ModItems.LEMON,
            ModItems.LEMON_SLICE,
            ModItems.LIME,
            ModItems.LIME_SLICE,
            ModItems.MANGO,
            ModItems.ORANGE,
            ModItems.ORANGE_SLICE,
            ModItems.PAPAYA,
            ModItems.PEACH,
            ModItems.PEAR,
            ModItems.PINEAPPLE,
            ModItems.RASPBERRY,
            ModItems.STRAWBERRY,
            ModItems.JAR,
            ModItems.EMPTY_JAR
        )
    }
}

private fun ItemModelGenerator.registerGenerated(vararg items: Item) {
    items.forEach {
        register(it, Models.GENERATED)
    }
}