package fruitmod.item.custom

import fruitmod.component.JamIngredientComponent
import fruitmod.component.ModDataComponents
import fruitmod.item.jam.ModJamIngredients
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class JamItem(settings: Settings): Item(settings) {

    override fun getDefaultStack(): ItemStack {
        val stack = super.defaultStack
        stack.set(ModDataComponents.JAM_INGREDIENTS, JamIngredientComponent(ModJamIngredients.STRAWBERRY))
        return stack
    }

    override fun getName(stack: ItemStack): Text {
        val ingredientsComponent = stack.get(ModDataComponents.JAM_INGREDIENTS)
        return ingredientsComponent?.getName(this.translationKey + ".jam_ingredient.") ?: super.getName(stack)
    }
}