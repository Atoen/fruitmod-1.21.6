package fruitmod.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class ModDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
        val pack = generator.createPack()

        pack.apply {
            addProvider(::ModTagGenerator)
            addProvider(::ModBlockLootTableProvider)
            addProvider(::ModModelGenerator)
            addProvider(::ModRecipeProvider)
        }
    }
}
