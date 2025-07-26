package fruitmod.item

import fruitmod.FruitMod
import fruitmod.component.JamBlockColorComponent
import fruitmod.component.ModDataComponents
import fruitmod.util.modIdentifier
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text

object ModItemGroups {

    val FRUITMOD_ITEM_GROUP_KEY = RegistryKey.of(
        Registries.ITEM_GROUP.key,
        modIdentifier("item_group")
    )!!

    val FRUITMOD_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
        FRUITMOD_ITEM_GROUP_KEY,
        FabricItemGroup.builder()
            .icon { ItemStack(ModItems.ORANGE) }
            .displayName(Text.translatable("itemgroup.fruitmod.mod_items"))
            .build())!!

    val FRUITMOD_BLOCK_GROUP_KEY = RegistryKey.of(
        Registries.ITEM_GROUP.key,
        modIdentifier("block_group")
    )

    val FRUITMOD_BLOCK_GROUP = Registry.register(Registries.ITEM_GROUP,
        FRUITMOD_BLOCK_GROUP_KEY,
        FabricItemGroup.builder()
            .icon {
                ItemStack(ModItems.JAM_BLOCK_ITEM).apply {
                    set(ModDataComponents.JAM_BLOCK_COLOR, JamBlockColorComponent.DEFAULT)
                }
            }
            .displayName(Text.translatable("itemgroup.fruitmod.mod_items"))
            .build())!!


    fun registerItemGroups() {
        FruitMod.logger.info("Registering Item Groups for ${FruitMod.MOD_ID}")
    }

    fun addJamBlocksToGroup() {
        generateBasicJamBlocks()
    }

    private fun generateBasicJamBlocks() {
        val basicColors = listOf(
            // Primary & Secondary Colors (RGB + YMC)
            Triple(15, 0, 0),    // Red
            Triple(0, 15, 0),    // Green
            Triple(0, 0, 15),    // Blue
            Triple(15, 15, 0),   // Yellow
            Triple(15, 0, 15),   // Magenta
            Triple(0, 15, 15),   // Cyan
            Triple(15, 15, 15),  // White
            Triple(0, 0, 0),     // Black

            // Fruit Colors
            Triple(13, 2, 4),    // Strawberry
            Triple(4, 0, 12),    // Blueberry
            Triple(15, 11, 0),   // Orange
            Triple(14, 14, 3),   // Banana (ripe yellow)
            Triple(9, 0, 9),     // Grape
            Triple(10, 5, 0),    // Mango (orange-brown)
            Triple(8, 10, 1),    // Pear (greenish-yellow)
            Triple(10, 0, 2),    // Cherry (deep red)
            Triple(11, 7, 3),    // Peach
            Triple(6, 8, 2),     // Green Apple
        )

        val jamBlockStacks = basicColors.map { (r, g, b) ->
            ItemStack(ModItems.JAM_BLOCK_ITEM).apply {
                set(ModDataComponents.JAM_BLOCK_COLOR, JamBlockColorComponent(r, g, b))
            }
        }

        val solidJamBlockStacks = basicColors.map { (r, g, b) ->
            ItemStack(ModItems.SOLID_JAM_BLOCK_ITEM).apply {
                set(ModDataComponents.JAM_BLOCK_COLOR, JamBlockColorComponent(r, g, b))
            }
        }

        ItemGroupEvents.modifyEntriesEvent(FRUITMOD_BLOCK_GROUP_KEY).register {
            it.addAll(jamBlockStacks)
            it.addAll(solidJamBlockStacks)
        }
    }
}