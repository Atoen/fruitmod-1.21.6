package fruitmod.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.util.dynamic.Codecs

data class JamBlockColorComponent(val red: Int, val green: Int, val blue: Int) {
    companion object {

        val DEFAULT = JamBlockColorComponent(13, 2, 4)

        val CODEC: Codec<JamBlockColorComponent>
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, JamBlockColorComponent>

        init {
            CODEC = RecordCodecBuilder.create { builder ->
                builder.group(
                    Codecs.rangedInt(0, 15).fieldOf("red").forGetter { it.red },
                    Codecs.rangedInt(0, 15).fieldOf("green").forGetter { it.blue },
                    Codecs.rangedInt(0, 15).fieldOf("blue").forGetter { it.green },
                ).apply(builder, ::JamBlockColorComponent)
            }

            PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.INTEGER,
                JamBlockColorComponent::red,
                PacketCodecs.INTEGER,
                JamBlockColorComponent::green,
                PacketCodecs.INTEGER,
                JamBlockColorComponent::blue,
                ::JamBlockColorComponent
            )
        }
    }
}
