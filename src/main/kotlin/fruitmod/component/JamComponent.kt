package fruitmod.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import fruitmod.item.jam.Jam
import fruitmod.item.jam.JamIngredient
import net.minecraft.component.ComponentsAccess
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

data class JamComponent(
    val jam: Optional<RegistryEntry<Jam>>,
    val ingredients: List<RegistryEntry<JamIngredient>>,
    val customColor: Optional<Int>,
    val additionalEffects: List<StatusEffectInstance>,
    val customName: Optional<String>,
    val portions: Int
) :  TooltipAppender {

    constructor(jam: RegistryEntry<Jam>) : this(
        Optional.of(jam),
        jam.value().ingredients,
        Optional.empty(),
        listOf(),
        Optional.empty(),
        DEFAULT_PORTIONS
    )

    val color: Int
        get() = getColor(DEFAULT_COLOR)

    fun getColor(defaultColor: Int): Int {
        return if (customColor.isPresent) {
            customColor.get()
        } else mixColors().orElse(defaultColor)
    }

    val effects: Iterable<StatusEffectInstance>
        get() {
            val ingredientsEffects = ingredients.flatMap { it.value().effects }
            return ingredientsEffects + additionalEffects
        }

    val name: Text
        get() {
            val name = customName.or {
                jam.map { it.value().name }
            }.orElse("Jamless")

            return Text.literal(name)
        }

    fun mixColors(): OptionalInt {
        if (ingredients.isEmpty()) {
            return OptionalInt.empty()
        }

        var red = 0
        var green = 0
        var blue = 0

        ingredients.forEach {
            val ingredientColor = it.value().color
            red += ColorHelper.getRed(ingredientColor)
            green += ColorHelper.getGreen(ingredientColor)
            blue += ColorHelper.getBlue(ingredientColor)
        }

        val ingredientsCount = ingredients.size
        return OptionalInt.of(
            ColorHelper.getArgb(
                red / ingredientsCount,
                green / ingredientsCount,
                blue / ingredientsCount
            )
        )
    }

    fun onConsume(
        world: World,
        user: LivingEntity,
        stack: ItemStack
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
                    0.0
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
        textConsumer.accept(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE))
        textConsumer.accept(Text.translatable("potion.whenDrank").formatted(Formatting.YELLOW))
        textConsumer.accept(Text.translatable("potion.whenDrank").formatted(Formatting.UNDERLINE))
    }

    companion object {
        const val DEFAULT_COLOR = 0xFC5A8D
        const val DEFAULT_PORTIONS = 4

        private val BASE_CODEC: Codec<JamComponent>
        val CODEC: Codec<JamComponent>
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, JamComponent>

        val DEFAULT = JamComponent(
            Optional.empty(),
            listOf(),
            Optional.empty(),
            listOf(),
            Optional.empty(),
            DEFAULT_PORTIONS
        )

        fun createStack(item: Item, jam: RegistryEntry<Jam>): ItemStack {
            return ItemStack(item).apply {
                set(ModDataComponents.JAMS, JamComponent(jam))
            }
        }

        init {
            BASE_CODEC = RecordCodecBuilder.create { builder ->
                builder.group(
                    Jam.CODEC.optionalFieldOf("jam").forGetter { it.jam },
                    JamIngredient.CODEC.listOf().optionalFieldOf("ingredients", listOf()).forGetter { it.ingredients },
                    Codec.INT.optionalFieldOf("custom_color").forGetter { it.customColor },
                    StatusEffectInstance.CODEC.listOf().optionalFieldOf("additional_effects", listOf()).forGetter { it.additionalEffects },
                    Codec.STRING.optionalFieldOf("custom_name").forGetter { it.customName },
                    Codec.INT.fieldOf("portions").forGetter { it.portions }
                ).apply(builder, ::JamComponent)
            }

            CODEC = Codec.withAlternative(BASE_CODEC, Jam.CODEC, ::JamComponent)

            PACKET_CODEC = PacketCodec.tuple(
                Jam.PACKET_CODEC.collect(PacketCodecs::optional),
                JamComponent::jam,
                JamIngredient.PACKET_CODEC.collect(PacketCodecs.toList()),
                JamComponent::ingredients,
                PacketCodecs.INTEGER.collect(PacketCodecs::optional),
                JamComponent::customColor,
                StatusEffectInstance.PACKET_CODEC.collect(PacketCodecs.toList()),
                JamComponent::additionalEffects,
                PacketCodecs.STRING.collect(PacketCodecs::optional),
                JamComponent::customName,
                PacketCodecs.VAR_INT,
                JamComponent::portions,
                ::JamComponent
            )
        }
    }
}