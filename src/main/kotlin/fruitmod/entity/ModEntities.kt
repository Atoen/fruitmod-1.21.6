package fruitmod.entity

import fruitmod.FruitMod
import fruitmod.util.modIdentifier
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

object ModEntities {

    val COCONUT = register("coconut", EntityType.Builder
        .create(::CoconutEntity, SpawnGroup.MISC)
        .dropsNothing()
        .dimensions(0.25f, 0.25f)
        .maxTrackingRange(4)
        .trackingTickInterval(10)
    )

    fun registerEntities() {
        FruitMod.logger.info("Registering Block Entities for {}", FruitMod.MOD_ID)
    }

    private fun <T : Entity> register(
        id: String,
        factory: EntityType.Builder<T>
    ): EntityType<T> {
        val key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, modIdentifier(id))
        return Registry.register(Registries.ENTITY_TYPE, key, factory.build(key))
    }
}
