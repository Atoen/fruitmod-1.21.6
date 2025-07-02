package fruitmod.item

import fruitmod.FruitMod
import fruitmod.block.ModBlocks
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.component.type.FoodComponent
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

object ModItems {

    val ORANGE = registerItem("orange", Item.Settings())

    val ORANGE_SLICE = registerItem("orange_slice", Item.Settings().food(
        FoodComponent.Builder()
            .nutrition(2)
            .saturationModifier(0.1f)
            .build()))

    val HONEY_BERRIES = registerItem("honey_berries",
        Item.Settings()
            .food(
                FoodComponent.Builder()
                    .nutrition(1)
                    .saturationModifier(0.1f)
                    .build()),
        factory = { BlockItem(ModBlocks.HONEY_BERRY_BUSH, it) }
    )

    private fun registerItem(
        name: String,
        settings: Item.Settings,
        factory: ((Item.Settings) -> Item) = { Item(it) }
    ): Item {
        val itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(FruitMod.MOD_ID, name))
        val item = factory(settings.registryKey(itemKey))

        return Registry.register(Registries.ITEM, itemKey, item)
    }

    fun registerModItems() {
        FruitMod.logger.info("Registering Mod Items for ${FruitMod.MOD_ID}")

        ItemGroupEvents.modifyEntriesEvent(ModItemGroups.FRUITMOD_ITEM_GROUP_KEY).register {
            it.apply {
                add { ORANGE }
                add { ORANGE_SLICE }
                add { HONEY_BERRIES }
            }
        }
    }
}