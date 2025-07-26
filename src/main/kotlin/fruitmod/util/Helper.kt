package fruitmod.util

import fruitmod.FruitMod
import net.minecraft.util.Identifier

fun modIdentifier(name: String): Identifier = Identifier.of(FruitMod.MOD_ID, name)