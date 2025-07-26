package fruitmod.util

import fruitmod.FruitMod
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object ModTags {

    fun registerTags() {
        FruitMod.logger.info("Registering Custom Tags for ${FruitMod.MOD_ID}")
    }

    object Blocks {

        val HEAT_SOURCES = createTag("heat_sources")

        private fun createTag(name: String): TagKey<Block> =
            TagKey.of(RegistryKeys.BLOCK, modIdentifier(name))
    }

    object Items {

        val FRUITFUL_ITEMS = createTag("fruitful_items")

        private fun createTag(name: String): TagKey<Item> =
            TagKey.of(RegistryKeys.ITEM, modIdentifier(name))
    }
}