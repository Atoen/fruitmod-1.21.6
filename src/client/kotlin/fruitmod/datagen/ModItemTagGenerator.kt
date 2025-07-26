package fruitmod.datagen

import fruitmod.block.ModBlocks
import fruitmod.item.ModItems
import fruitmod.util.modIdentifier
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagBuilder
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

internal class ModItemTagGenerator(
    output: FabricDataOutput,
    completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider.ItemTagProvider(output, completableFuture) {

    val FRUITY_ITEMS = createTag("fruity_items")

    private fun createTag(name: String): TagKey<Item> =
        TagKey.of(RegistryKeys.ITEM, modIdentifier(name))

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        getTagBuilder(FRUITY_ITEMS)
            .add(ModItems.ORANGE)
            .add(ModItems.ORANGE_SLICE)

        getTagBuilder(ItemTags.LOGS_THAT_BURN)
            .add(ModBlocks.DRIFTWOOD_LOG)
            .add(ModBlocks.STRIPPED_DRIFTWOOD_LOG)
            .add(ModBlocks.STRIPPED_DRIFTWOOD_WOOD)
            .add(ModBlocks.DRIFTWOOD_WOOD)

        getTagBuilder(ItemTags.PLANKS)
            .add(ModBlocks.DRIFTWOOD_PLANKS)
    }
}

private fun TagBuilder.add(item: Item): TagBuilder {
    return this.add(Registries.ITEM.getId(item))
}

private fun TagBuilder.add(block: Block): TagBuilder {
    return this.add(Registries.ITEM.getId(block.asItem()))
}
