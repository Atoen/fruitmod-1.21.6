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
data class JamTintSource(
    val defaultColor: Int = JamComponent.DEFAULT_COLOR
) : TintSource {

    override fun getTint(
        stack: ItemStack,
        world: ClientWorld?,
        user: LivingEntity?
    ): Int {
        val colorInt = stack.get(ModDataComponents.JAM)?.color ?: defaultColor
        return ColorHelper.fullAlpha(colorInt)
    }

    override fun getCodec() = CODEC

    companion object {
        val CODEC: MapCodec<JamTintSource> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Codecs.RGB.fieldOf("default").forGetter(JamTintSource::defaultColor)
            ).apply(builder, ::JamTintSource)
        }
    }
}
