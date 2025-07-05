package fruitmod.sound

import fruitmod.FruitMod
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier

object ModSounds {

    val BONK = registerSoundEvent("bonk")

    fun registerSounds() {
        FruitMod.logger.info("Registering sounds for ${FruitMod.MOD_ID}")
    }

    private fun registerSoundEvent(name: String): SoundEvent {
        val id = Identifier.of(FruitMod.MOD_ID, name)
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id))
    }
}