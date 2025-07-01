package fruitmod.item

import fruitmod.FruitMod
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object ModItemGroups {

    val FRUITMOD_ITEM_GROUP_KEY = RegistryKey.of(
        Registries.ITEM_GROUP.key,
        Identifier.of(FruitMod.MOD_ID, "item_group")
    )!!

    val FRUITMOD_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
        FRUITMOD_ITEM_GROUP_KEY,
        FabricItemGroup.builder()
            .icon { ItemStack(ModItems.ORANGE) }
            .displayName(Text.translatable("itemgroup.fruitmod.mod_items"))
            .build())

    fun registerItemGroups() {
        FruitMod.logger.info("Registering Item Groups for ${FruitMod.MOD_ID}")
    }
}