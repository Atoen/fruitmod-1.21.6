package fruitmod.sound

import fruitmod.FruitMod
import fruitmod.util.modIdentifier
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent

object ModSounds {

    val BONK = registerSoundEvent("bonk")

    fun registerSounds() {
        FruitMod.logger.info("Registering sounds for {}", FruitMod.MOD_ID)
    }

    private fun registerSoundEvent(name: String): SoundEvent {
        val id = modIdentifier(name)
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id))
    }
}