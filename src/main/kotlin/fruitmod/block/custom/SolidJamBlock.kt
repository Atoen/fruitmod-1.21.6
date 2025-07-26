package fruitmod.block.custom

import com.mojang.serialization.MapCodec

class SolidJamBlock(settings: Settings) : BaseJamBlock(settings) {

    override fun getCodec() = CODEC

    companion object {
        val CODEC: MapCodec<SolidJamBlock> = createCodec(::SolidJamBlock)
    }
}