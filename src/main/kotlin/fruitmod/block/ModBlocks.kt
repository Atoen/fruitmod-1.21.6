package fruitmod.block

import fruitmod.FruitMod
import fruitmod.item.ModItemGroups
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

object ModBlocks {

    val COMPACTED_DIRT = registerBlock("compacted_dirt", AbstractBlock.Settings.create()
        .strength(4f)
        .requiresTool()
        .sounds(BlockSoundGroup.MUD))

    private fun registerBlock(
        name: String,
        settings: AbstractBlock.Settings,
        alsoRegisterItem: Boolean = true
    ): Block {
        val blockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(FruitMod.MOD_ID, name))
        val block = Block(settings.registryKey(blockKey))

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
            it.add { COMPACTED_DIRT.asItem() }
        }
    }
}