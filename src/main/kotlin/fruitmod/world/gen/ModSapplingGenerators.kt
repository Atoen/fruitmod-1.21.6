package fruitmod.world.gen

import fruitmod.FruitMod
import fruitmod.world.ModConfiguredFeatures
import net.minecraft.block.SaplingGenerator
import java.util.Optional

object ModSaplingGenerators {
    val DRIFTWOOD = SaplingGenerator(
        "${FruitMod.MOD_ID}:driftwood",
        Optional.empty(),
        Optional.of(ModConfiguredFeatures.DRIFTWOOD_KEY),
        Optional.empty()
    )
}