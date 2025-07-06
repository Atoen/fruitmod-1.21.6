package fruitmod

import com.mojang.serialization.Codec
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos



object ModComponents {

    val COORDINATES = register("coordinates") {
        it.codec(BlockPos.CODEC)
    }

    val JAM_INGREDIENTS = register("jam_ingredients") {
        it.codec(Codec.list(Identifier.CODEC))
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

    fun registerComponentTypes() {
        FruitMod.logger.info("Registering Component Types for ${FruitMod.MOD_ID}")
    }
}