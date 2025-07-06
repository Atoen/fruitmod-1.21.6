package fruitmod.block

import fruitmod.FruitMod
import fruitmod.block.custom.DriftwoodLeavesBlock
import fruitmod.block.custom.HoneyBerryBushBlock
import fruitmod.item.ModItemGroups
import fruitmod.world.gen.ModSaplingGenerators
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.PillarBlock
import net.minecraft.block.SaplingBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

object ModBlocks {

    val COMPACTED_DIRT = registerBlock("compacted_dirt", createSettings()
        .strength(4f)
        .requiresTool()
        .sounds(BlockSoundGroup.MUD))

    val HONEY_BERRY_BUSH = registerBlock("honey_berry_bush",
        copySettings(Blocks.SWEET_BERRY_BUSH),
        ::HoneyBerryBushBlock,
        alsoRegisterItem = false
    )

    val DRIFTWOOD_LOG = registerBlock("driftwood_log",
        copySettings(Blocks.OAK_LOG),
        ::PillarBlock
    )

    val DRIFTWOOD_WOOD = registerBlock("driftwood_wood",
        copySettings(Blocks.OAK_WOOD),
        ::PillarBlock
    )

    val STRIPPED_DRIFTWOOD_LOG = registerBlock("stripped_driftwood_log",
        AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_LOG),
        ::PillarBlock
    )

    val STRIPPED_DRIFTWOOD_WOOD = registerBlock("stripped_driftwood_wood",
        copySettings(Blocks.STRIPPED_OAK_WOOD),
        ::PillarBlock
    )

    val DRIFTWOOD_PLANKS = registerBlock("driftwood_planks",
        copySettings(Blocks.OAK_PLANKS))

    val DRIFTWOOD_LEAVES = registerBlock("driftwood_leaves",
        copySettings(Blocks.OAK_LEAVES),
        ::DriftwoodLeavesBlock
    )

    val DRIFTWOOD_SAPLING = registerBlock("driftwood_sapling",
        copySettings(Blocks.OAK_SAPLING),
        { SaplingBlock(ModSaplingGenerators.DRIFTWOOD, it) }
    )

    private fun registerBlock(
        name: String,
        settings: AbstractBlock.Settings,
        factory: ((AbstractBlock.Settings) -> Block) = { Block(it) },
        alsoRegisterItem: Boolean = true
    ): Block {
        val blockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(FruitMod.MOD_ID, name))
        val block = factory(settings.registryKey(blockKey))

        if (alsoRegisterItem) {
            val itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(FruitMod.MOD_ID, name))
            val blocKItem = BlockItem(block, Item.Settings().registryKey(itemKey))

            Registry.register(Registries.ITEM, itemKey, blocKItem)
        }

        return Registry.register(Registries.BLOCK, blockKey, block)
    }

    fun registerModBlocks() {
        FruitMod.logger.info("Registering Mod Blocks for ${FruitMod.MOD_ID}")

        ItemGroupEvents.modifyEntriesEvent(ModItemGroups.FRUITMOD_ITEM_GROUP_KEY).register {
            it.run {
                add { COMPACTED_DIRT.asItem() }
                add { DRIFTWOOD_SAPLING.asItem() }
                add { DRIFTWOOD_LEAVES.asItem() }
                add { DRIFTWOOD_PLANKS.asItem() }
                add { DRIFTWOOD_LOG.asItem() }
                add { DRIFTWOOD_WOOD.asItem() }
                add { STRIPPED_DRIFTWOOD_WOOD.asItem() }
                add { STRIPPED_DRIFTWOOD_LOG.asItem() }
            }
        }
    }
}

private fun createSettings() = AbstractBlock.Settings.create()

private fun copySettings(block: Block): AbstractBlock.Settings {
    return AbstractBlock.Settings.copy(block)
}
