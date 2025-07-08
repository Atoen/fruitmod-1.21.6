package fruitmod

import com.mojang.serialization.Lifecycle
import fruitmod.item.jam.Jam
import fruitmod.item.jam.JamIngredient
import net.minecraft.Bootstrap
import net.minecraft.registry.*
import net.minecraft.registry.entry.RegistryEntryInfo
import net.minecraft.util.Identifier

object ModRegistries {

    val JAM_INGREDIENT_REGISTRY_KEY: RegistryKey<Registry<JamIngredient>> = RegistryKey.ofRegistry(Identifier.of(FruitMod.MOD_ID, "jam_ingredient"))
    val JAM_INGREDIENT_REGISTRY: Registry<JamIngredient>

    val JAM_REGISTRY_KEY: RegistryKey<Registry<Jam>> = RegistryKey.ofRegistry(Identifier.of(FruitMod.MOD_ID, "jam"))
    val JAM_REGISTRY: Registry<Jam>

    init {
        JAM_INGREDIENT_REGISTRY = create(JAM_INGREDIENT_REGISTRY_KEY)
        JAM_REGISTRY = create(JAM_REGISTRY_KEY)
    }

    fun addModRegistries() {
        FruitMod.logger.info("Adding Mod Registries for ${FruitMod.MOD_ID}")
    }

    private fun <T> create(key: RegistryKey<out Registry<T>>): Registry<T> {
        return create(
            key,
            SimpleRegistry(key, Lifecycle.stable(), false)
        )
    }

    @Suppress("unchecked_cast")
    private fun <T, R : MutableRegistry<T>> create(
        key: RegistryKey<out Registry<T>>,
        registry: R
    ): R {
        val identifier = key.value
        Bootstrap.ensureBootstrapped { "registry $identifier" }

        val rootRegistry = Registries.REGISTRIES as MutableRegistry<MutableRegistry<*>>
        val castedKey = key as RegistryKey<MutableRegistry<*>>

        rootRegistry.add(castedKey, registry, RegistryEntryInfo.DEFAULT)

        return registry
    }
}