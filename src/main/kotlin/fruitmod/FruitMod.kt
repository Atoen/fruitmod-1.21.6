package fruitmod

import fruitmod.block.ModBlocks
import fruitmod.item.ModItemGroups
import fruitmod.item.ModItems
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object FruitMod : ModInitializer {
    val logger = LoggerFactory.getLogger("fruitmod")!!
	const val MOD_ID = "fruitmod"

	override fun onInitialize() {
		ModItemGroups.registerItemGroups()
		ModItems.registerModItems()
		ModBlocks.registerModBlocks()
	}
}