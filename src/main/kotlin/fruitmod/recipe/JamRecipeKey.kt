package fruitmod.recipe

import net.minecraft.item.Item
import net.minecraft.item.ItemStack

@JvmInline
value class JamRecipeKey(val value: Int) {
    companion object {

        private val PRIMES = intArrayOf(
            13, 17, 19, 23, 29, 31, 37, 41, 43
        )

        fun createKey(jamBase: ItemStack, ingredients: List<ItemStack>): JamRecipeKey {
            val baseId = Item.getRawId(jamBase.item)

            var nonEmptyCount = 0
            val ids = IntArray(ingredients.size) {
                val stack = ingredients[it]
                if (!stack.isEmpty) nonEmptyCount++
                Item.getRawId(stack.item)
            }

            when (nonEmptyCount) {
                0 -> return JamRecipeKey(baseId)
                else -> ids.sort()
            }

            var hash = baseId * PRIMES[0]
            for (i in 0..<nonEmptyCount) {
                val prime = PRIMES[i % PRIMES.size]
                hash = hash xor (ids[i] * prime)
            }

            return JamRecipeKey(hash)
        }
    }
}
