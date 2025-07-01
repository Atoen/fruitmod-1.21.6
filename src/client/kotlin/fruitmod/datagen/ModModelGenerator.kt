package fruitmod.datagen

import fruitmod.block.ModBlocks
import fruitmod.item.ModItems
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.client.data.BlockStateModelGenerator
import net.minecraft.client.data.ItemModelGenerator
import net.minecraft.client.data.Models
import net.minecraft.item.Item

class ModModelGenerator(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        generator.registerSimpleCubeAll(ModBlocks.COMPACTED_DIRT)
    }

    override fun generateItemModels(generator: ItemModelGenerator) {
        generator.registerGenerated(
            ModItems.ORANGE, ModItems.ORANGE_SLICE
        )
    }
}

private fun ItemModelGenerator.registerGenerated(vararg items: Item) {
    items.forEach {
        register(it, Models.GENERATED)
    }
}