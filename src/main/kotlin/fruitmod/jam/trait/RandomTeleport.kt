package fruitmod.jam.trait

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import fruitmod.jam.JamData
import fruitmod.jam.ingredient.JamIngredients
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.consume.TeleportRandomlyConsumeEffect
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.function.Consumer

data class RandomTeleport(val baseDiameter: Float = 16f) : JamActionTrait {
    override val type = TraitTypes.RANDOM_TELEPORT

    override fun appendTooltip(context: Item.TooltipContext, textConsumer: Consumer<Text>, tooltipType: TooltipType) {
        val text = Text.translatable("trait.fruitmod.random_teleport.tooltip").formatted(Formatting.BLUE)
        textConsumer.accept(text)
    }

    override fun performAction(world: ServerWorld, entity: LivingEntity, jam: JamData) {
        val chorusFruitsUsed = jam.ingredients.count { it == JamIngredients.CHORUS_FRUIT }
        val diameter = (baseDiameter * chorusFruitsUsed).coerceAtLeast(baseDiameter)

        TeleportRandomlyConsumeEffect(diameter).onConsume(world, ItemStack.EMPTY, entity)
    }

    companion object {
        val CODEC: MapCodec<RandomTeleport> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Codec.FLOAT.fieldOf("diameter").forGetter { it.baseDiameter }
            ).apply(builder, ::RandomTeleport)
        }

        val PACKET_CODEC: PacketCodec<in RegistryByteBuf, RandomTeleport> =
            PacketCodecs.FLOAT.xmap(::RandomTeleport, RandomTeleport::baseDiameter)
    }
}
