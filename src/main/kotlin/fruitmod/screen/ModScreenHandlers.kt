package fruitmod.screen

import fruitmod.FruitMod
import fruitmod.screen.custom.JamStationScreenHandler
import fruitmod.screen.custom.PedestalScreenHandler
import fruitmod.util.modIdentifier
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.math.BlockPos

object ModScreenHandlers {

    val PEDESTAL_SCREEN_HANDLER = registerExtended("pedestal_screen_handler",
        ::PedestalScreenHandler,
        BlockPos.PACKET_CODEC
    )

    val JAM_STATION_SCREEN_HANDLER = register("jam_station",
        ::JamStationScreenHandler
    )

    fun registerScreenHandles() {
        FruitMod.logger.info("Registering Screen Handlers for {}", FruitMod.MOD_ID)
    }

    private fun <THandler : ScreenHandler, TData> registerExtended(
        name: String,
        factory: ExtendedScreenHandlerType.ExtendedFactory<THandler, TData>,
        packetCodec: PacketCodec<in RegistryByteBuf, TData>
    ): ExtendedScreenHandlerType<THandler, TData> {
        return Registry.register(Registries.SCREEN_HANDLER, modIdentifier(name),
            ExtendedScreenHandlerType(factory, packetCodec)
        )
    }

    private fun <THandler : ScreenHandler> register(
        name: String,
        factory: ScreenHandlerType.Factory<THandler>
    ): ScreenHandlerType<THandler> {
        return Registry.register(Registries.SCREEN_HANDLER, modIdentifier(name),
            ScreenHandlerType(factory, FeatureFlags.VANILLA_FEATURES)
        )
    }
}