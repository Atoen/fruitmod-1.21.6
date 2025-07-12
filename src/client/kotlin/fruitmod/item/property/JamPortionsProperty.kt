package fruitmod.item.property

import com.mojang.serialization.MapCodec
import fruitmod.item.custom.JamItem
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.item.property.select.SelectProperty
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemDisplayContext
import net.minecraft.item.ItemStack
import net.minecraft.util.StringIdentifiable

@Environment(EnvType.CLIENT)
class JamPortionsProperty : SelectProperty<JamItem.Portions> {

    override fun getValue(
        stack: ItemStack,
        world: ClientWorld?,
        user: LivingEntity?,
        seed: Int,
        displayContext: ItemDisplayContext
    ): JamItem.Portions {
        val portions = JamItem.getRemainingPortions(stack)

        return when {
            portions <= 0 -> JamItem.Portions.NONE
            portions == 1 -> JamItem.Portions.ONE
            portions == 2 -> JamItem.Portions.TWO
            portions == 3 -> JamItem.Portions.THREE
            else -> JamItem.Portions.FOUR_OR_MORE
        }
    }

    override fun valueCodec() = VALUE_CODEC

    override fun getType() = TYPE

    companion object {
        val VALUE_CODEC: StringIdentifiable.EnumCodec<JamItem.Portions> = JamItem.Portions.CODEC
        val TYPE: SelectProperty.Type<JamPortionsProperty, JamItem.Portions> = SelectProperty.Type.create(
            MapCodec.unit(JamPortionsProperty()),
            VALUE_CODEC
        )
    }
}
