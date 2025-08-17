package fruitmod.jam.crafted

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import fruitmod.component.JamComponent
import fruitmod.jam.JamData
import fruitmod.jam.trait.JamTrait
import fruitmod.jam.base.JamBase
import fruitmod.jam.base.JamBases
import fruitmod.jam.ingredient.JamIngredient
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Rarity
import net.minecraft.util.dynamic.Codecs
import java.util.Optional

data class CraftedJam(
    override val base: RegistryEntry<JamBase>,
    override val ingredients: List<RegistryEntry<JamIngredient>>,
    override val additionalEffects: List<StatusEffectInstance> = emptyList(),
    override val customNameKey: Optional<String> = Optional.empty(),
    override val customColor: Optional<Int> = Optional.empty(),
    override val rarity: Rarity = Rarity.COMMON,
    override val hasGlint: Boolean = rarity >= Rarity.RARE,
    override val maxPortions: Int = JamComponent.DEFAULT_PORTIONS
) : JamData {

    val allEffects: List<StatusEffectInstance> = createEffectList()

    override val traits: List<JamTrait> = createTraitList()

    inline fun <reified T : JamTrait> traitsOfType(): List<T> {
        return traits.filterIsInstance<T>()
    }

    private fun createEffectList(): List<StatusEffectInstance> {
        val ingredientEffects = ingredients.flatMap { it.value().effects }
        val allEffects = ingredientEffects + additionalEffects

        return allEffects
            .groupBy { it.effectType }
            .map { (_, group) ->
                group.maxWith(
                    compareBy<StatusEffectInstance> { it.amplifier }
                        .thenBy { it.duration }
                )
            }
    }

    private fun createTraitList(): List<JamTrait> {
        val baseTraits = base.value().traits
        val ingredientTraits = ingredients.mapNotNull {
            it.value().trait.orElse(null)
        }

        val allTraits = baseTraits + ingredientTraits
        val seenKeys = mutableSetOf<Any>()

        return buildList {
            allTraits.forEach {
                val key = it.deduplicationKey
                if (seenKeys.add(key)) add(it)
            }
        }
    }

    companion object {

        const val DEFAULT_COLOR = 0xFC5A8D

        val DEFAULT = CraftedJam(
            base = JamBases.SUGAR,
            ingredients = emptyList(),
            customNameKey = Optional.of("fruitmod.jam.uncraftable"),
            customColor = Optional.of(DEFAULT_COLOR)
        )

        fun sugarBase(
            ingredients: List<RegistryEntry<JamIngredient>>,
            additionalEffects: List<StatusEffectInstance> = emptyList(),
            customNameKey: Optional<String> = Optional.empty(),
            customColor: Optional<Int> = Optional.empty(),
            rarity: Rarity = Rarity.COMMON,
            hasGlint: Boolean = rarity >= Rarity.RARE,
            maxPortions: Int = JamComponent.DEFAULT_PORTIONS
        ) = CraftedJam(
            JamBases.SUGAR,
            ingredients,
            additionalEffects,
            customNameKey,
            customColor,
            rarity,
            hasGlint,
            maxPortions
        )

        val CODEC: Codec<CraftedJam> = RecordCodecBuilder.create { builder ->
            builder.group(
                JamBase.Companion.CODEC.fieldOf("base").forGetter { it.base },
                JamIngredient.Companion.CODEC.listOf().fieldOf("ingredients").forGetter { it.ingredients },
                StatusEffectInstance.CODEC.listOf().fieldOf("effects").forGetter { it.additionalEffects },
                Codec.STRING.optionalFieldOf("custom_name_key").forGetter { it.customNameKey },
                Codecs.RGB.optionalFieldOf("custom_color").forGetter { it.customColor },
                Rarity.CODEC.optionalFieldOf("rarity", Rarity.COMMON).forGetter { it.rarity },
                Codec.BOOL.optionalFieldOf("has_glint", false).forGetter { it.hasGlint },
                Codec.INT.optionalFieldOf("max_portions", JamComponent.DEFAULT_PORTIONS).forGetter { it.maxPortions }
            ).apply(builder, ::CraftedJam)
        }

        val PACKET_CODEC: PacketCodec<RegistryByteBuf, CraftedJam> = PacketCodec.tuple(
            JamBase.Companion.PACKET_CODEC, CraftedJam::base,
            JamIngredient.Companion.PACKET_CODEC.collect(PacketCodecs.toList()), CraftedJam::ingredients,
            StatusEffectInstance.PACKET_CODEC.collect(PacketCodecs.toList()), CraftedJam::additionalEffects,
            PacketCodecs.STRING.collect(PacketCodecs::optional), CraftedJam::customNameKey,
            PacketCodecs.RGB.collect(PacketCodecs::optional), CraftedJam::customColor,
            Rarity.PACKET_CODEC, CraftedJam::rarity,
            PacketCodecs.BOOLEAN, CraftedJam::hasGlint,
            PacketCodecs.VAR_INT, CraftedJam::maxPortions,
            ::CraftedJam
        )
    }
}
