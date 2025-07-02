package fruitmod

import fruitmod.datagen.ModBlockLootTableProvider
import fruitmod.datagen.ModBlockTagGenerator
import fruitmod.datagen.ModItemTagGenerator
import fruitmod.datagen.ModModelGenerator
import fruitmod.datagen.ModRecipeProvider
import fruitmod.datagen.ModRegistryDataGenerator
import fruitmod.world.ModConfigureFeatures
import fruitmod.world.ModPlacedFeatures
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.RegistryBuilder
import net.minecraft.registry.RegistryKeys

class ModDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
        val pack = generator.createPack()

        pack.apply {
            addProvider(::ModItemTagGenerator)
            addProvider(::ModBlockTagGenerator)
            addProvider(::ModBlockLootTableProvider)
            addProvider(::ModModelGenerator)
            addProvider(::ModRecipeProvider)
            addProvider(::ModRegistryDataGenerator)
        }
    }

    override fun buildRegistry(registryBuilder: RegistryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfigureFeatures::bootstrap)
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
    }
}