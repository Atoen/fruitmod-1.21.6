package fruitmod.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import fruitmod.jam.JamData
import fruitmod.jam.crafted.CraftedJam
import fruitmod.jam.trait.JamActionTrait
import fruitmod.jam.trait.JamModifierTrait
import fruitmod.util.Color3i
import fruitmod.util.JamColorComponent
import fruitmod.util.getFullTextFormatted
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
import net.minecraft.screen.ScreenTexts
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.text.Texts
import net.minecraft.util.Formatting
import net.minecraft.util.math.ColorHelper
import net.minecraft.world.World
import java.util.*
import java.util.function.Consumer

data class JamComponent(
    val jam: CraftedJam,
    val portions: Int = jam.maxPortions
) : TooltipAppender, JamColorComponent, JamData by jam {

    override val channels get() = Color3i.fromRGB(color)

    val color get() = getColor(DEFAULT_COLOR)

    val effects = applyEffectsModifiers(jam.allEffects, jam.traitsOfType<JamModifierTrait>())
    val hasEffects = !effects.isEmpty()

    fun getColor(defaultColor: Int): Int {
        return if (customColor.isPresent) {
            customColor.get()
        } else mixColors().orElse(defaultColor)
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
        val ingredientsColor = ColorHelper.getArgb(
            red / ingredientsCount,
            green / ingredientsCount,
            blue / ingredientsCount
        )

        val jamColor = base.value().applyColor(ingredientsColor)
        return OptionalInt.of(jamColor)
    }

    val name: Text get() {
        if (customNameKey.isPresent) {
            return Text.translatable(customNameKey.get())
        }

        return createName()
    }

    private fun createName(): Text {
        return when (ingredients.size) {
            0 -> Text.translatable("jam.fruitmod.no_ingredients")
            1 -> {
                val ingredient = ingredients[0].value()
                Text.translatable(
                    "jam.fruitmod.one_fruit_jam",
                    ingredient.translatableText
                )
            }
            2 -> {
                val first = ingredients[0].value()
                val second = ingredients[1].value()
                Text.translatable(
                    "jam.fruitmod.two_fruits_jam",
                    first.translatableText,
                    second.translatableText,
                )
            }
            else -> Text.translatable("jam.fruitmod.mixed_fruit")
        }
    }

    fun onConsume(
        world: World,
        user: LivingEntity
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

        traits.forEach {
            if (it is JamActionTrait) {
                it.performAction(world, user, jam)
            }
        }
    }

    private fun applyEffectsModifiers(
        effects: List<StatusEffectInstance>,
        modifierTraits: List<JamModifierTrait>
    ): List<StatusEffectInstance> {
        if (modifierTraits.isEmpty()) {
            return effects
        }

        return modifierTraits.fold(effects) { acc, trait ->
            trait.applyModifier(acc)
        }
    }

    override fun appendTooltip(
        context: Item.TooltipContext,
        textConsumer: Consumer<Text>,
        tooltipType: TooltipType,
        components: ComponentsAccess
    ) {
        // Portions
        val portionsText = Text.translatable("jam.fruitmod.portions_left", portions).formatted(Formatting.GRAY)
        textConsumer.accept(portionsText)
        textConsumer.accept(ScreenTexts.EMPTY)

        // Base
        val baseText = Text.translatable("jam.fruitmod.base", base.value().translatableText)
        textConsumer.accept(baseText)

        // Ingredients
        if (!ingredients.isEmpty()) {
            val ingredientTexts = ingredients.map {
                it.value().translatableText
            }

            val joined = Texts.join(ingredientTexts, Text.literal(", "))
            textConsumer.accept(joined)
        }

        // Effects
        textConsumer.accept(ScreenTexts.EMPTY)
        if (hasEffects) {
            textConsumer.accept(Text.translatable("jam.fruitmod.effects").formatted(Formatting.DARK_PURPLE))

            effects.forEach {
                val effectText = it.getFullTextFormatted(context.updateTickRate)
                textConsumer.accept(effectText)
            }
        } else {
            textConsumer.accept(NO_EFFECT_TEXT)
        }

        // Traits
        textConsumer.accept(ScreenTexts.EMPTY)
        traits.forEach {
            it.appendTooltip(context, textConsumer, tooltipType)
        }
    }

    companion object {

        private val NO_EFFECT_TEXT = Text.translatable("effect.none").formatted(Formatting.GRAY)

        const val DEFAULT_PORTIONS = 4
        const val DEFAULT_COLOR = 0xFC5A8D

        val DEFAULT = JamComponent(CraftedJam.DEFAULT)

        fun createStack(item: Item, jam: CraftedJam): ItemStack {
            return ItemStack(item).apply {
                set(ModDataComponents.JAM, JamComponent(jam))
            }
        }

        val CODEC: Codec<JamComponent> = RecordCodecBuilder.create { builder ->
            builder.group(
                CraftedJam.CODEC.fieldOf("jam_data").forGetter { it.jam },
                Codec.INT.optionalFieldOf("portions", DEFAULT_PORTIONS).forGetter { it.portions }
            ).apply(builder, ::JamComponent)
        }

        val PACKET_CODEC: PacketCodec<RegistryByteBuf, JamComponent> = PacketCodec.tuple(
            CraftedJam.PACKET_CODEC, JamComponent::jam,
            PacketCodecs.VAR_INT, JamComponent::portions,
            ::JamComponent
        )
    }
}
