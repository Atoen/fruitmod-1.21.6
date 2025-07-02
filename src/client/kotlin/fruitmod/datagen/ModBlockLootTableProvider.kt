package fruitmod.datagen

import fruitmod.block.ModBlocks
import fruitmod.block.custom.HoneyBerryBushBlock
import fruitmod.item.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Blocks
import net.minecraft.enchantment.Enchantments
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.BlockStatePropertyLootCondition
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.ApplyBonusLootFunction
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.predicate.StatePredicate
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture


internal class ModBlockLootTableProvider(
    output: FabricDataOutput,
    completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricBlockLootTableProvider(output, completableFuture) {

    override fun generate() {

        val enchantments = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT)

        addDrop(
            Blocks.DIRT,
            drops(Blocks.NETHERITE_BLOCK)
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1f, 5f)))
        )

        addDrop(ModBlocks.HONEY_BERRY_BUSH) { block ->
            applyExplosionDecay(
                block, LootTable.builder()
                    .pool(LootPool.builder().conditionally(
                        BlockStatePropertyLootCondition.builder(ModBlocks.HONEY_BERRY_BUSH)
                                .properties(StatePredicate.Builder.create().exactMatch(HoneyBerryBushBlock.AGE, 3))
                            )
                        .with(ItemEntry.builder(ModItems.HONEY_BERRIES))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2f, 3f)))
                        .apply(ApplyBonusLootFunction.uniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
                ).pool(LootPool.builder().conditionally(
                        BlockStatePropertyLootCondition.builder(ModBlocks.HONEY_BERRY_BUSH).properties(StatePredicate
                            .Builder.create().exactMatch(HoneyBerryBushBlock.AGE, 2))

                ).with(ItemEntry.builder(ModItems.HONEY_BERRIES))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1f, 2f)))
                        .apply(ApplyBonusLootFunction.uniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
                )
            )
        }

        addDrop(ModBlocks.DRIFTWOOD_LOG)
        addDrop(ModBlocks.DRIFTWOOD_WOOD)
        addDrop(ModBlocks.STRIPPED_DRIFTWOOD_LOG)
        addDrop(ModBlocks.STRIPPED_DRIFTWOOD_WOOD)
        addDrop(ModBlocks.DRIFTWOOD_PLANKS)
        addDrop(ModBlocks.DRIFTWOOD_SAPLING)

        addDrop(ModBlocks.DRIFTWOOD_LEAVES,
            leavesDrops(ModBlocks.DRIFTWOOD_LEAVES, ModBlocks.DRIFTWOOD_SAPLING, 0.0625f)
        )
    }
}