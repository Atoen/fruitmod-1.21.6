package fruitmod

import com.mojang.serialization.Lifecycle
import fruitmod.item.jam.Jam
import fruitmod.item.jam.JamBase
import fruitmod.item.jam.JamIngredient
import fruitmod.item.jam.SpecialJam
import fruitmod.util.modIdentifier
import net.minecraft.Bootstrap
import net.minecraft.registry.*
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntryInfo

object ModRegistries {

    val JAM_INGREDIENT_REGISTRY_KEY: RegistryKey<Registry<JamIngredient>> = RegistryKey.ofRegistry(modIdentifier("jam_ingredient"))
    val JAM_INGREDIENT: Registry<JamIngredient>

    val JAM_BASE_REGISTRY_KEY: RegistryKey<Registry<JamBase>> = RegistryKey.ofRegistry(modIdentifier("jam_base"))
    val JAM_BASE: Registry<JamBase>

    val JAM_REGISTRY_KEY: RegistryKey<Registry<Jam>> = RegistryKey.ofRegistry(modIdentifier("jam"))
    val JAM: Registry<Jam>

    val SPECIAL_JAM_REGISTRY_KEY: RegistryKey<Registry<SpecialJam>> = RegistryKey.ofRegistry(modIdentifier("special_jam"))
    val SPECIAL_JAM: Registry<SpecialJam>

    init {
        JAM_INGREDIENT = create(JAM_INGREDIENT_REGISTRY_KEY)
        JAM = create(JAM_REGISTRY_KEY)
        JAM_BASE = create(JAM_BASE_REGISTRY_KEY)
        SPECIAL_JAM = create(SPECIAL_JAM_REGISTRY_KEY)
    }

    fun addModRegistries() {
        FruitMod.logger.info("Adding Mod Registries for {}", FruitMod.MOD_ID)
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