package fruitmod.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Blocks
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

internal class ModBlockLootTableProvider(
    output: FabricDataOutput,
    completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricBlockLootTableProvider(output, completableFuture) {

    override fun generate() {
        addDrop(
            Blocks.DIRT,
            drops(Blocks.NETHERITE_BLOCK)
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1f, 5f)))
        )
    }
}