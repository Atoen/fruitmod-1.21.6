package fruitmod.datagen

import fruitmod.FruitMod
import fruitmod.item.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagBuilder
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

internal class ModTagGenerator(
    output: FabricDataOutput,
    completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider.ItemTagProvider(output, completableFuture) {

    val FRUITY_ITEMS = createTag("fruity_items")

    private fun createTag(name: String): TagKey<Item> =
        TagKey.of(RegistryKeys.ITEM, Identifier.of(FruitMod.MOD_ID, name))

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        getTagBuilder(FRUITY_ITEMS)
            .add(ModItems.ORANGE)
            .add(ModItems.ORANGE_SLICE)
    }
}

private fun TagBuilder.add(item: Item): TagBuilder {
    return this.add(Registries.ITEM.getId(item))
}