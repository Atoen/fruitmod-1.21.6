package fruitmod.block

import fruitmod.block.custom.PlacedFruitBlock
import fruitmod.block.custom.PlacedFruitBlockWithEntity
import fruitmod.block.entity.custom.EnchantedGoldenAppleBlockEntity
import fruitmod.item.ModItems
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block.createCuboidShape
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.item.Items
import net.minecraft.sound.BlockSoundGroup

object FruitBlocks {
    object Shapes {
        val APPLE = createCuboidShape(4.0, 0.0, 4.0, 12.0, 8.0, 12.0)
        val ORANGE = createCuboidShape(4.0, 0.0, 4.0, 12.0, 8.0, 12.0)
    }

    fun defaultSettings(): AbstractBlock.Settings = AbstractBlock.Settings.create()
        .strength(0.2f)
        .nonOpaque()
        .pistonBehavior(PistonBehavior.DESTROY)
        .sounds(BlockSoundGroup.WET_GRASS)

    class Apple(settings: Settings) : PlacedFruitBlock(settings, Items.APPLE, Shapes.APPLE) {
        override fun getCodec() = CODEC
        companion object {
            val CODEC = createCodec(::Apple)
        }
    }

    class GoldenApple(settings: Settings) : PlacedFruitBlock(settings, Items.GOLDEN_APPLE, Shapes.APPLE) {
        override fun getCodec() = CODEC
        companion object {
            val CODEC = createCodec(::GoldenApple)
        }
    }

    class EnchantedGoldenApple(settings: Settings) : PlacedFruitBlockWithEntity(
        settings,
        Items.ENCHANTED_GOLDEN_APPLE,
        Shapes.APPLE,
        ::EnchantedGoldenAppleBlockEntity
    ) {
        override fun getCodec() = CODEC
        companion object {
            val CODEC = createCodec(::EnchantedGoldenApple)
        }
    }

    class Orange(settings: Settings) : PlacedFruitBlock(settings, ModItems.ORANGE, Shapes.ORANGE) {
        override fun getCodec() = CODEC
        companion object {
            val CODEC = createCodec(::Orange)
        }
    }
}
