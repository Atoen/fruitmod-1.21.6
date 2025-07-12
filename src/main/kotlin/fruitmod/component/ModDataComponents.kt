package fruitmod.component

import fruitmod.FruitMod
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModDataComponents {

    val JAMS = register("jams") {
        it.codec(JamComponent.CODEC).packetCodec(JamComponent.PACKET_CODEC).cache()
    }

    val JAM_CONSUMABLE = register("jam_consumable") {
        it.codec(JamConsumableComponent.CODEC).packetCodec(JamConsumableComponent.PACKET_CODEC).cache()
    }

    private fun <T> register(
        name: String,
        builder: (ComponentType.Builder<T>) -> ComponentType.Builder<T>
    ) : ComponentType<T> {
        return Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(FruitMod.MOD_ID, name),
            builder(ComponentType.builder<T>()).build()
        )
    }

    fun registerDataComponentTypes() {
        FruitMod.logger.info("Registering Data Component Types for ${FruitMod.MOD_ID}")
    }
}
