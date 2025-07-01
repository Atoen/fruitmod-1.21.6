package fruitmod.util

import fruitmod.FruitMod
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object ModTags {
    object Blocks {
        private fun createTag(name: String): TagKey<Block> =
            TagKey.of(RegistryKeys.BLOCK, Identifier.of(FruitMod.MOD_ID, name))
    }

    object Items {

        val FRUITFUL_ITEMS = createTag("fruitful_items")

        private fun createTag(name: String): TagKey<Item> =
            TagKey.of(RegistryKeys.ITEM, Identifier.of(FruitMod.MOD_ID, name))
    }
}