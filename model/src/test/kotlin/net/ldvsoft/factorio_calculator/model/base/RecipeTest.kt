package net.ldvsoft.factorio_calculator.model.base

import java.time.Duration
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Recipe tests")
internal class RecipeTest {
    val a = Item("a")
    val b = Item("b")

    val m = MachineType("m")
    val n = MachineType("n")
    val machineLevelMap = mapOf(n to 1)

    val m0 = Machine("m0", m, 0, 1.0)
    val m1 = Machine("m1", m, 1, 2.0)
    val n0 = Machine("n0", n, 0, 1.0)
    val n1 = Machine("n1", n, 1, 2.0)

    val recipe = Recipe("1",
            ItemCounts(1 of a), ItemCounts(5 of b),
            Duration.ofSeconds(1), machineLevelMap)

    @Test
    @DisplayName("Can be crafted? test")
    fun canBeCraftedTest() {
        assertFalse(recipe.canBeCraftedIn(m0))
        assertFalse(recipe.canBeCraftedIn(m1))
        assertFalse(recipe.canBeCraftedIn(n0))

        assertTrue(recipe.canBeCraftedIn(n1))
    }

    @Test
    @DisplayName("Output test")
    fun getOutputTest() {
        val zero = ItemCounts()
        assertEquals(zero, recipe.getOutputWith(n1, zero).filerNonZero())
        assertEquals(recipe.products, recipe.getOutputWith(n1, recipe.ingredients))

        assertThrows(IllegalArgumentException::class.java) { recipe.getOutputWith(n0, zero) }
    }

    @Test
    @DisplayName("Output flow test")
    fun getOutputFlowTest() {
        assertEquals(ItemCounts(50 of b), recipe.getMaximumOutputFlowWith(n1, Duration.ofSeconds(5)))

        assertThrows(IllegalArgumentException::class.java) { recipe.getMaximumOutputFlowWith(n0, Duration.ZERO) }
    }
}