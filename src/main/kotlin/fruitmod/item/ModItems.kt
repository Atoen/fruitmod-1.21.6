package fruitmod.item

import fruitmod.FruitMod
import fruitmod.ModRegistries
import fruitmod.block.ModBlocks
import fruitmod.component.JamBlockColorComponent
import fruitmod.component.JamComponent
import fruitmod.component.JamConsumableComponent
import fruitmod.component.ModDataComponents
import fruitmod.item.custom.*
import fruitmod.item.food.ModConsumableComponents
import fruitmod.item.food.ModFoodComponents
import fruitmod.util.modIdentifier
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.FoodComponents
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

object ModItems {

    val BANANA = registerItem("banana", Item.Settings().food(ModFoodComponents.HIGH_NUTRITION_FRUIT, ModConsumableComponents.BANANA))

    val BLUEBERRY = registerItem("blueberry", Item.Settings().food(ModFoodComponents.LOW_NUTRITION_FRUIT, ModConsumableComponents.LOW_NUTRITION_FRUIT))
    val CHERRIES = registerItem("cherries", Item.Settings().food(ModFoodComponents.LOW_NUTRITION_FRUIT, ModConsumableComponents.FAST_FRUIT))
    val COCONUT = registerItem("coconut", factory = ::CoconutItem)
    val OPEN_COCONUT = registerItem("open_coconut", Item.Settings().food(ModFoodComponents.BASIC_FRUIT, ModConsumableComponents.HYDRATING_FRUIT), ::OpenCoconutItem)

    val GRAPES = registerItem("grapes", Item.Settings().food(ModFoodComponents.LOW_NUTRITION_FRUIT, ModConsumableComponents.FAST_FRUIT))
    val HONEY_BERRIES = registerItem("honey_berries", Item.Settings().food(ModFoodComponents.LOW_NUTRITION_FRUIT, ModConsumableComponents.FAST_FRUIT), factory = { BlockItem(ModBlocks.HONEY_BERRY_BUSH, it) })

    val KIWI = registerItem("kiwi", Item.Settings().food(ModFoodComponents.BASIC_FRUIT, ModConsumableComponents.JUICY_FRUIT))
    val LEMON = registerItem("lemon", Item.Settings().food(ModFoodComponents.LOW_NUTRITION_FRUIT, ModConsumableComponents.SOUR_SLICE))
    val LEMON_SLICE = registerItem("lemon_slice", Item.Settings().food(ModFoodComponents.SLICE, ModConsumableComponents.SOUR_SLICE))

    val LIME = registerItem("lime", Item.Settings().food(ModFoodComponents.LOW_NUTRITION_FRUIT, ModConsumableComponents.SOUR_SLICE))
    val LIME_SLICE = registerItem("lime_slice", Item.Settings().food(ModFoodComponents.SLICE, ModConsumableComponents.SOUR_SLICE))

    val MANGO = registerItem("mango", Item.Settings().food(ModFoodComponents.JUICY_FRUIT, ModConsumableComponents.JUICY_FRUIT))
    val ORANGE = registerItem("orange", Item.Settings().food(ModFoodComponents.BASIC_FRUIT, ModConsumableComponents.JUICY_FRUIT))
    val ORANGE_SLICE = registerItem("orange_slice", Item.Settings().food(ModFoodComponents.SLICE, ModConsumableComponents.SLICE))

    val PAPAYA = registerItem("papaya", Item.Settings().food(ModFoodComponents.JUICY_FRUIT, ModConsumableComponents.BASIC_FRUIT))
    val PEACH = registerItem("peach", Item.Settings().food(ModFoodComponents.JUICY_FRUIT, ModConsumableComponents.JUICY_FRUIT))
    val PEAR = registerItem("pear", Item.Settings().food(ModFoodComponents.BASIC_FRUIT, ModConsumableComponents.BASIC_FRUIT))
    val PINEAPPLE = registerItem("pineapple", Item.Settings().food(ModFoodComponents.JUICY_FRUIT, ModConsumableComponents.PINEAPPLE))

    val RASPBERRY = registerItem("raspberry", Item.Settings().food(ModFoodComponents.LOW_NUTRITION_FRUIT, ModConsumableComponents.FAST_FRUIT))
    val STRAWBERRY = registerItem("strawberry", Item.Settings().food(ModFoodComponents.LOW_NUTRITION_FRUIT, ModConsumableComponents.FAST_FRUIT))

    val JAR = registerItem("jar")
    val EMPTY_JAR = registerItem("empty_jar")

    val JAM = registerItem(
        "jam",
        Item.Settings()
            .maxCount(1)
            .recipeRemainder(JAR)
            .component(ModDataComponents.JAM, JamComponent.DEFAULT)
            .component(ModDataComponents.JAM_CONSUMABLE, JamConsumableComponent.DEFAULT)
            .component(DataComponentTypes.FOOD, FoodComponents.HONEY_BOTTLE),
        ::JamItem
    )

    val JAM_BLOCK_ITEM = registerItem(
        "jam_block_item",
        Item.Settings()
            .component(ModDataComponents.JAM_BLOCK_COLOR, JamBlockColorComponent.DEFAULT),
        factory = { JamBlockItem(ModBlocks.JAM_BLOCK, it) }
    )

    val SOLID_JAM_BLOCK_ITEM = registerItem(
        "solid_jam_block_item",
        Item.Settings()
            .component(ModDataComponents.JAM_BLOCK_COLOR, JamBlockColorComponent.DEFAULT),
        factory = { SolidJamBlockItem(ModBlocks.SOLID_JAM_BLOCK, it) }
    )

    private fun generateJams() {
        val jamStacks = ModRegistries.JAM_REGISTRY.streamEntries()
            .map { JamComponent.createStack(JAM, it) }
            .toList()

        ItemGroupEvents.modifyEntriesEvent(ModItemGroups.FRUITMOD_ITEM_GROUP_KEY).register { entries ->
            entries.addAll(jamStacks)
        }
    }

    private fun registerItem(
        name: String,
        settings: Item.Settings = Item.Settings(),
        factory: ((Item.Settings) -> Item) = { Item(it) }
    ): Item {
        val itemKey = RegistryKey.of(RegistryKeys.ITEM, modIdentifier(name))
        val item = factory(settings.registryKey(itemKey))

        return Registry.register(Registries.ITEM, itemKey, item)
    }

    fun registerModItems() {
        FruitMod.logger.info("Registering Mod Items for ${FruitMod.MOD_ID}")

        ItemGroupEvents.modifyEntriesEvent(ModItemGroups.FRUITMOD_ITEM_GROUP_KEY).register {
            it.add(
                BANANA, BLUEBERRY, CHERRIES, COCONUT, OPEN_COCONUT,
                GRAPES, HONEY_BERRIES, KIWI, LEMON, LEMON_SLICE,
                LIME, LIME_SLICE, MANGO, ORANGE, ORANGE_SLICE,
                PAPAYA, PEACH, PEAR, PINEAPPLE, RASPBERRY, STRAWBERRY,
                JAR, EMPTY_JAR
            )
        }

        generateJams()
    }
}

private fun FabricItemGroupEntries.add(vararg items: Item) {
    items.forEach {
        add { it }
    }
}