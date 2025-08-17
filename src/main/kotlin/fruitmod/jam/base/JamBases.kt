package fruitmod.jam.base

import fruitmod.FruitMod
import fruitmod.ModRegistries
import fruitmod.jam.recipe.JamRecipeRegistry
import fruitmod.jam.trait.*
import fruitmod.util.modIdentifier
import fruitmod.util.toRegistryEntry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import java.util.*

object JamBases {

    val SUGAR = registerBase("sugar", Items.SUGAR)

    val GLOWSTONE_DUST = registerBase(
        "glowstone", Items.GLOWSTONE_DUST,
        listOf(
            ScaleEffectDuration(1.5f),
            AddEffect(StatusEffectInstance(StatusEffects.GLOWING, 40 * 20))
        )
    )

    val BLAZE_POWDER = registerBase(
        "blaze_powder", Items.BLAZE_POWDER,
        listOf(
            AddEffect(StatusEffectInstance(StatusEffects.SPEED, 20 * 20)),
            SetOnFire()
        )
    )

    val GUNPOWDER = registerBase(
        "gunpowder", Items.GUNPOWDER,
        listOf(
            Explode()
        )
    )

    val REDSTONE = registerBase(
        "redstone", Items.REDSTONE,
        listOf(
            RandomTeleport(64f)
        )
    )

    fun registerJamBases() {
        FruitMod.logger.info("Registering Jam Bases for {}", FruitMod.MOD_ID)
    }

    private fun registerBase(
        id: String,
        item: Item,
        traits: List<JamTrait> = emptyList(),
        customNameKey: String? = null
    ): RegistryEntry<JamBase> {
        val base = JamBase(item.toRegistryEntry(), traits, Optional.ofNullable(customNameKey))
        return Registry.registerReference(
            ModRegistries.JAM_BASE,
            modIdentifier(id),
            base
        ).also {
            JamRecipeRegistry.registerBase(item, it)
        }
    }
}
