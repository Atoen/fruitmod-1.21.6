package fruitmod.datagen

import fruitmod.block.ModBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagBuilder
import java.util.concurrent.CompletableFuture

internal class ModBlockTagGenerator(
    output: FabricDataOutput,
    completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider.BlockTagProvider(output, completableFuture) {

    override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
        getTagBuilder(BlockTags.LOGS_THAT_BURN)
            .add(ModBlocks.DRIFTWOOD_LOG)
            .add(ModBlocks.STRIPPED_DRIFTWOOD_LOG)
            .add(ModBlocks.STRIPPED_DRIFTWOOD_WOOD)
            .add(ModBlocks.DRIFTWOOD_WOOD)

        getTagBuilder(BlockTags.CLIMBABLE)
            .add(ModBlocks.JAM_BLOCK)

        getTagBuilder(BlockTags.SHOVEL_MINEABLE)
            .add(ModBlocks.JAM_BLOCK)
            .add(ModBlocks.SOLID_JAM_BLOCK)
    }
}

private fun TagBuilder.add(block: Block): TagBuilder {
    return this.add(Registries.BLOCK.getId(block))
}