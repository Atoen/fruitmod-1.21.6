package fruitmod.datagen

import fruitmod.item.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.recipe.RecipeExporter
import net.minecraft.data.recipe.RecipeGenerator
import net.minecraft.recipe.book.RecipeCategory
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
        // Orange -> 4 Orange Slices
        createShapeless(RecipeCategory.FOOD, ModItems.ORANGE_SLICE, 4)
            .input(ModItems.ORANGE)
            .criterion(hasItem(ModItems.ORANGE), conditionsFromItem(ModItems.ORANGE))
            .offerTo(exporter)

        // Lemon -> 4 Lemon Slices
        createShapeless(RecipeCategory.FOOD, ModItems.LEMON_SLICE, 4)
            .input(ModItems.LEMON)
            .criterion(hasItem(ModItems.LEMON), conditionsFromItem(ModItems.LEMON))
            .offerTo(exporter)

        // Lime -> 3 Lime Slices
        createShapeless(RecipeCategory.FOOD, ModItems.LIME_SLICE, 3)
            .input(ModItems.LIME)
            .criterion(hasItem(ModItems.LIME), conditionsFromItem(ModItems.LIME))
            .offerTo(exporter)

        // Coconut -> Open Coconut
        createShapeless(RecipeCategory.FOOD, ModItems.OPEN_COCONUT)
            .input(ModItems.COCONUT)
            .criterion(hasItem(ModItems.COCONUT), conditionsFromItem(ModItems.COCONUT))
            .offerTo(exporter)
    }
}