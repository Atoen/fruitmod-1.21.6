package fruitmod

import fruitmod.util.modIdentifier
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object ModTags {

    fun registerTags() {
        FruitMod.logger.info("Registering Custom Tags for {}", FruitMod.MOD_ID)
    }

    object Blocks {

        val HEAT_SOURCES = createTag("heat_sources")

        private fun createTag(name: String): TagKey<Block> =
            TagKey.of(RegistryKeys.BLOCK, modIdentifier(name))
    }

    object Items {

        val JAM_BASE = createTag("jam_base")
        val JAM_INGREDIENT = createTag("jam_ingredient")

        private fun createTag(name: String): TagKey<Item> =
            TagKey.of(RegistryKeys.ITEM, modIdentifier(name))
    }
}