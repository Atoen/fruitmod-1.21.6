package fruitmod.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import fruitmod.item.custom.JamBlockItem
import net.minecraft.inventory.CraftingInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.recipe.book.RecipeBookCategories
import net.minecraft.recipe.input.CraftingRecipeInput
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.World

data class JamMergingRecipe(
    val inputItem: Ingredient, val output: ItemStack
) : SpecialCraftingRecipe(
    CraftingRecipeCategory.MISC
) {

    override fun matches(
        input: CraftingRecipeInput,
        world: World
    ): Boolean {
        if (world.isClient) {
            return false
        }
        TODO("Not yet implemented")
    }

    override fun craft(
        input: CraftingRecipeInput,
        registries: RegistryWrapper.WrapperLookup
    ): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getSerializer() = ModRecipes.JAM_MERGING_SERIALIZER

    override fun getRecipeBookCategory() = RecipeBookCategories.CRAFTING_BUILDING_BLOCKS

    class Serializer : RecipeSerializer<JamMergingRecipe> {

        companion object {
            val CODEC: MapCodec<JamMergingRecipe> = RecordCodecBuilder.mapCodec { builder ->
                builder.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(JamMergingRecipe::inputItem),
                    ItemStack.CODEC.fieldOf("result").forGetter(JamMergingRecipe::output)
                ).apply(builder, ::JamMergingRecipe)
            }

            val PACKET_CODEC: PacketCodec<RegistryByteBuf, JamMergingRecipe> = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, JamMergingRecipe::inputItem,
                ItemStack.PACKET_CODEC, JamMergingRecipe::output,
                ::JamMergingRecipe
            )
        }

        override fun codec() = CODEC

        @Deprecated("Deprecated in Java")
        override fun packetCodec() = PACKET_CODEC
    }
}