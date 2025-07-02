package fruitmod.world.gen

import fruitmod.FruitMod
import fruitmod.world.ModConfigureFeatures
import net.minecraft.block.SaplingGenerator
import java.util.Optional

object ModSaplingGenerators {
    val DRIFTWOOD = SaplingGenerator(
        "${FruitMod.MOD_ID}:driftwood",
        Optional.empty(),
        Optional.of(ModConfigureFeatures.DRIFTWOOD_KEY),
        Optional.empty()
    )
}