package fruitmod

import fruitmod.block.ModBlocks
import fruitmod.component.ModDataComponents
import fruitmod.item.ModItemGroups
import fruitmod.item.ModItems
import fruitmod.item.jam.Jams
import fruitmod.item.jam.ModJamIngredients
import fruitmod.particle.ModParticles
import fruitmod.sound.ModSounds
import fruitmod.world.gen.ModWorldGeneration
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import org.slf4j.LoggerFactory

object FruitMod : ModInitializer {
	const val MOD_ID = "fruitmod"
	val logger = LoggerFactory.getLogger(MOD_ID)!!

	override fun onInitialize() {
		ModItemGroups.registerItemGroups()
		ModSounds.registerSounds()
		ModParticles.registerParticles()

		ModWorldGeneration.generateModWorldGen()

		ModRegistries.addModRegistries()
		ModDataComponents.registerDataComponentTypes()
		ModJamIngredients.registerJamIngredients()
		Jams.registerJams()

		ModItems.registerModItems()
		ModBlocks.registerModBlocks()

		StrippableBlockRegistry.register(ModBlocks.DRIFTWOOD_LOG, ModBlocks.STRIPPED_DRIFTWOOD_LOG)
		StrippableBlockRegistry.register(ModBlocks.DRIFTWOOD_WOOD, ModBlocks.STRIPPED_DRIFTWOOD_WOOD)

		FlammableBlockRegistry.getDefaultInstance().run {
			add(ModBlocks.DRIFTWOOD_LOG, 5, 5)
			add(ModBlocks.DRIFTWOOD_WOOD, 5, 5)
			add(ModBlocks.STRIPPED_DRIFTWOOD_LOG, 5, 5)
			add(ModBlocks.STRIPPED_DRIFTWOOD_WOOD, 5, 5)
			add(ModBlocks.DRIFTWOOD_PLANKS, 5, 20)
			add(ModBlocks.DRIFTWOOD_LEAVES, 30, 60)
		}
    }
}