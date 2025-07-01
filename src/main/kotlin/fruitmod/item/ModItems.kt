package fruitmod.item

import fruitmod.FruitMod
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.component.type.FoodComponent
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

    private fun registerItem(
        name: String,
        settings: Item.Settings
    ): Item {
        val itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(FruitMod.MOD_ID, name))
        val item = Item(settings.registryKey(itemKey))

        return Registry.register(Registries.ITEM, itemKey, item)
    }

    fun registerModItems() {
        FruitMod.logger.info("Registering Mod Items for ${FruitMod.MOD_ID}")

        ItemGroupEvents.modifyEntriesEvent(ModItemGroups.FRUITMOD_ITEM_GROUP_KEY).register {
            it.add { ORANGE }
            it.add { ORANGE_SLICE }
        }
    }
}