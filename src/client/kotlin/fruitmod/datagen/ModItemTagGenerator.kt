package fruitmod.datagen

import fruitmod.ModTags
import fruitmod.block.ModBlocks
import fruitmod.item.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagBuilder
import java.util.concurrent.CompletableFuture

internal class ModItemTagGenerator(
    output: FabricDataOutput,
    completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider.ItemTagProvider(output, completableFuture) {

    override fun configure(arg: RegistryWrapper.WrapperLookup) {

        getTagBuilder(ItemTags.LOGS_THAT_BURN)
            .add(ModBlocks.DRIFTWOOD_LOG)
            .add(ModBlocks.STRIPPED_DRIFTWOOD_LOG)
            .add(ModBlocks.STRIPPED_DRIFTWOOD_WOOD)
            .add(ModBlocks.DRIFTWOOD_WOOD)

        getTagBuilder(ItemTags.PLANKS)
            .add(ModBlocks.DRIFTWOOD_PLANKS)

        getTagBuilder(ModTags.Items.JAM_BASE)
            .add(Items.SUGAR, Items.GLOWSTONE_DUST)

        getTagBuilder(ModTags.Items.JAM_INGREDIENT)
            .add(Items.APPLE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE)
            .add(Items.SWEET_BERRIES, Items.GLOW_BERRIES)
            .add(Items.MELON_SLICE, Items.GLISTERING_MELON_SLICE)
            .add(Items.CHORUS_FRUIT)
            .add(
                ModItems.ORANGE_SLICE, ModItems.PEAR, ModItems.KIWI, ModItems.PAPAYA, ModItems.RASPBERRY,
                ModItems.STRAWBERRY, ModItems.BANANA, ModItems.BLUEBERRY, ModItems.PINEAPPLE,
                ModItems.PEACH, ModItems.GRAPES, ModItems.OPEN_COCONUT, ModItems.CHERRIES,
                ModItems.LIME_SLICE
            )
    }
}

private fun TagBuilder.add(item: Item): TagBuilder {
    return add(Registries.ITEM.getId(item))
}

private fun TagBuilder.add(vararg items: Item): TagBuilder {
    items.forEach(::add)
    return this
}

private fun TagBuilder.add(block: Block): TagBuilder {
    return add(Registries.ITEM.getId(block.asItem()))
}
