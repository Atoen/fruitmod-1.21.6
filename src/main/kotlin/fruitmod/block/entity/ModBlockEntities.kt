package fruitmod.block.entity

import fruitmod.FruitMod
import fruitmod.block.ModBlocks
import fruitmod.block.entity.custom.JamStationBlockEntity
import fruitmod.block.entity.custom.PedestalBlockEntity
import fruitmod.util.modIdentifier
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModBlockEntities {

    val PEDESTAL_BLOCK_ENTITY = register("pedestal_be", ::PedestalBlockEntity, ModBlocks.PEDESTAL)

    val JAM_STATION = register("jam_station", ::JamStationBlockEntity, ModBlocks.JAM_STATION)

    fun registerBlockEntities() {
        FruitMod.logger.info("Registering Block Entities for {}", FruitMod.MOD_ID)
    }

    private fun <T : BlockEntity> register(
        name: String,
        factory: FabricBlockEntityTypeBuilder.Factory<out T>,
        block: Block
    ): BlockEntityType<T> {
        return Registry.register(
            Registries.BLOCK_ENTITY_TYPE, modIdentifier(name),
            FabricBlockEntityTypeBuilder.create(factory, block)
                .build()
        )
    }
}