package fruitmod.datagen

import fruitmod.item.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.recipe.RecipeExporter
import net.minecraft.data.recipe.RecipeGenerator
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: FabricDataOutput,
    completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricRecipeProvider(output, completableFuture) {

    override fun getRecipeGenerator(
        registryLookup: RegistryWrapper.WrapperLookup,
        exporter: RecipeExporter
    ): RecipeGenerator {
        return ModRecipeGenerator(registryLookup, exporter)
    }

    override fun getName() = "FruitModRecipeProvider"
}

private class ModRecipeGenerator(
    registryLookup: RegistryWrapper.WrapperLookup,
    exporter: RecipeExporter
) : RecipeGenerator(registryLookup, exporter) {

    override fun generate() {
        val itemLookup = registries.getOrThrow(RegistryKeys.ITEM)

        createShapeless(RecipeCategory.FOOD, ModItems.ORANGE_SLICE, 4)
            .input(ModItems.ORANGE)
            .criterion(hasItem(ModItems.ORANGE), conditionsFromItem(ModItems.ORANGE))
            .offerTo(exporter)
    }
}