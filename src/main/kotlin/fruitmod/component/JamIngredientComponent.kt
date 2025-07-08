package fruitmod.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import fruitmod.item.jam.JamIngredient
import net.minecraft.component.ComponentsAccess
import net.minecraft.component.type.Consumable
import net.minecraft.component.type.ConsumableComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipAppender
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.ColorHelper
import net.minecraft.world.World
import java.util.*
import java.util.function.Consumer

data class JamIngredientComponent(
    val jamIngredient: Optional<RegistryEntry<JamIngredient>>,
    val customColor: Optional<Int>,
    val customEffects: List<StatusEffectInstance>,
    val customName: Optional<String>
) : Consumable, TooltipAppender {

    constructor(jamIngredient: RegistryEntry<JamIngredient>) : this(
        Optional.of(jamIngredient), Optional.empty(), listOf(), Optional.empty()
    )

    val color: Int
        get() = getColor(DEFAULT_COLOR)

    fun getColor(defaultColor: Int): Int {
        return if (customColor.isPresent) {
            customColor.get()
        } else {
            mixColors(effects).orElse(defaultColor)
        }
    }

    val effects: Iterable<StatusEffectInstance>
        get() {
            val ingredientEffects = jamIngredient.orElse(null)?.value()?.effects.orEmpty()

            return when {
                jamIngredient.isEmpty -> customEffects
                customEffects.isEmpty() -> ingredientEffects
                else -> ingredientEffects + customEffects
            }
        }

    fun getName(prefix: String): Text {
        val text = this.customName.or {
            this.jamIngredient.map { it.value().name }
        }.orElse("empty")

        return Text.literal(text)
    }

    fun mixColors(effects: Iterable<StatusEffectInstance>): OptionalInt {
        var red = 0
        var green = 0
        var blue = 0
        var totalWeight = 0

        for (effect in effects) {
            if (effect.shouldShowParticles()) {
                val color = effect.effectType.value().color
                val weight = effect.amplifier + 1
                red += weight * ColorHelper.getRed(color)
                green += weight * ColorHelper.getGreen(color)
                blue += weight * ColorHelper.getBlue(color)
                totalWeight += weight
            }
        }

        return if (totalWeight == 0) {
            OptionalInt.empty()
        } else {
            OptionalInt.of(ColorHelper.getArgb(red / totalWeight, green / totalWeight, blue / totalWeight))
        }
    }


    override fun onConsume(
        world: World,
        user: LivingEntity,
        stack: ItemStack,
        consumable: ConsumableComponent
    ) {
        if (world !is ServerWorld) return
        val playerEntity = user as? PlayerEntity

        effects.forEach {
            val effectType = it.effectType.value()
            if (effectType.isInstant) {
                effectType.applyInstantEffect(
                    world,
                    playerEntity,
                    playerEntity,
                    user,
                    it.amplifier,
                    1.0
                )
            } else {
                user.addStatusEffect(it)
            }
        }
    }

    override fun appendTooltip(
        context: Item.TooltipContext,
        textConsumer: Consumer<Text>,
        type: TooltipType,
        components: ComponentsAccess
    ) {

    }

    companion object {

        fun createStack(item: Item, jamIngredient: RegistryEntry<JamIngredient>): ItemStack {
            val itemStack = ItemStack(item)
            itemStack.set(ModDataComponents.JAM_INGREDIENTS, JamIngredientComponent(jamIngredient))
            return itemStack
        }

        val DEFAULT = JamIngredientComponent(
            Optional.empty(),
            Optional.empty(),
            listOf(),
            Optional.empty()
        )

        val DEFAULT_COLOR = 0xFC5A8D

        private val BASE_CODEC: Codec<JamIngredientComponent>
        val CODEC: Codec<JamIngredientComponent>
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, JamIngredientComponent>

        init {

            BASE_CODEC = RecordCodecBuilder.create { builder ->
                builder.group(
                    JamIngredient.CODEC.optionalFieldOf("jam_ingredient").forGetter { it.jamIngredient },
                    Codec.INT.optionalFieldOf("custom_color").forGetter { it.customColor },
                    StatusEffectInstance.CODEC.listOf().optionalFieldOf("custom_effects", listOf()).forGetter { it.customEffects },
                    Codec.STRING.optionalFieldOf("custom_name").forGetter { it.customName }
                ).apply(builder, ::JamIngredientComponent)
            }

            CODEC = Codec.withAlternative(BASE_CODEC, JamIngredient.CODEC, ::JamIngredientComponent)

            PACKET_CODEC = PacketCodec.tuple(
                JamIngredient.PACKET_CODEC.collect(PacketCodecs::optional),
                JamIngredientComponent::jamIngredient,
                PacketCodecs.INTEGER.collect(PacketCodecs::optional),
                JamIngredientComponent::customColor,
                StatusEffectInstance.PACKET_CODEC.collect(PacketCodecs.toList()),
                JamIngredientComponent::customEffects,
                PacketCodecs.STRING.collect(PacketCodecs::optional),
                JamIngredientComponent::customName,
                ::JamIngredientComponent
            )
        }
    }
}