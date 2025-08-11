package fruitmod.component

import fruitmod.FruitMod
import fruitmod.util.modIdentifier
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModDataComponents {

    val JAM = register("jam") {
        it.codec(JamComponent.CODEC).packetCodec(JamComponent.PACKET_CODEC).cache()
    }

    val JAM_CONSUMABLE = register("jam_consumable") {
        it.codec(JamConsumableComponent.CODEC).packetCodec(JamConsumableComponent.PACKET_CODEC).cache()
    }

    val JAM_BLOCK_COLOR = register("jam_block_color") {
        it.codec(JamBlockColorComponent.CODEC).packetCodec(JamBlockColorComponent.PACKET_CODEC).cache()
    }

    private fun <T> register(
        name: String,
        builder: (ComponentType.Builder<T>) -> ComponentType.Builder<T>
    ) : ComponentType<T> {
        return Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            modIdentifier(name),
            builder(ComponentType.builder<T>()).build()
        )
    }

    fun registerDataComponentTypes() {
        FruitMod.logger.info("Registering Data Component Types for {}", FruitMod.MOD_ID)
    }
}
