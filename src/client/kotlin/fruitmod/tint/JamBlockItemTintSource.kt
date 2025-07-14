package fruitmod.tint

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import fruitmod.component.JamComponent
import fruitmod.component.ModDataComponents
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.item.tint.TintSource
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.dynamic.Codecs
import net.minecraft.util.math.ColorHelper

@Environment(EnvType.CLIENT)
data class JamBlockItemTintSource(
    val defaultColor: Int = JamComponent.DEFAULT_COLOR
) : TintSource {

    override fun getTint(
        stack: ItemStack,
        world: ClientWorld?,
        user: LivingEntity?
    ): Int {
        val colorInt = stack.get(ModDataComponents.JAM_BLOCK_COLOR)?.run {
            ColorHelper.getArgb(
                red * COMPONENT_COLOR_SCALE_FACTOR,
                green * COMPONENT_COLOR_SCALE_FACTOR,
                blue * COMPONENT_COLOR_SCALE_FACTOR,
            )
        } ?: defaultColor

        return colorInt
    }

    override fun getCodec() = CODEC

    companion object {

        private const val COMPONENT_COLOR_SCALE_FACTOR = 17

        val CODEC: MapCodec<JamBlockItemTintSource> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Codecs.RGB.fieldOf("default").forGetter { it.defaultColor }
            ).apply(builder, ::JamBlockItemTintSource)
        }
    }
}
