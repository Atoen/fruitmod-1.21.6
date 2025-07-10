package fruitmod.tint

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import fruitmod.component.JamIngredientComponent
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
data class JamIngredientTintSource(
    val defaultColor: Int = JamIngredientComponent.DEFAULT_COLOR
) : TintSource {

    override fun getTint(
        stack: ItemStack,
        world: ClientWorld?,
        user: LivingEntity?
    ): Int {
        val colorInt = stack.get(ModDataComponents.JAM_INGREDIENTS)?.color ?: defaultColor
        return ColorHelper.fullAlpha(colorInt)
    }

    override fun getCodec(): MapCodec<out TintSource> = CODEC

    companion object {
        val CODEC: MapCodec<JamIngredientTintSource> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Codecs.RGB.fieldOf("default").forGetter(JamIngredientTintSource::defaultColor)
            ).apply(builder, ::JamIngredientTintSource)
        }
    }
}
