package net.ldvsoft.factorio_calculator.model.base

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ItemCounts tests")
internal class ItemCountsTest {
    @Test
    @DisplayName("Equality test")
    fun equalityTest() {
        val a = Item("a")
        val b = Item("b")
        val c = Item("c")

        val x = ItemCounts(1.2 of a, 3.6 of b)
        val y = ItemCounts(1.2 of a, 3.6 of b)
        val z = ItemCounts(1.2 of a, 3.6 of b, 0 of c)
        val d = ItemCounts(1.3 of a, 3.6 of b)

        assertNotEquals(x, Unit)

        assertTrue(z.contains(c))
        assertFalse(z.filerNonZero().contains(c))

        assertEquals(x, y)
        assertNotEquals(x, z)
        assertNotEquals(z, x)
        assertEquals(x, z.filerNonZero())
        assertNotEquals(x, d)
        assertNotEquals(d, x)
    }

    @Test
    @DisplayName("Addition & subtraction test")
    fun additionAndSubtractionTest() {
        val zero = ItemCounts()
        val a = Item("a")
        val b = Item("b")
        val c = Item("c")

        val x = ItemCounts(1.2 of a, 3.6 of b)
        val y = ItemCounts(4.5 of a, 6.7 of c)

        assertEquals(ItemCounts(5.7 of a, 3.6 of b, 6.7 of c), x + y)
        assertEquals(x, x + zero)

        assertEquals(ItemCounts(-3.3 of a, 3.6 of b, -6.7 of c), x - y)
        assertEquals(y, y - zero)

        assertNotEquals(zero, x - x)
        assertEquals(ItemCounts(0 of a, 0 of b), x - x)

        assertEquals(-x, zero - x)
    }

    @Test
    @DisplayName("Number multiplication and division test")
    fun numberMultiplicationAndDivisionTest() {
        val zero = ItemCounts()
        val a = Item("a")
        val b = Item("b")

        val x = ItemCounts(5 of a, 2.1 of b)

        assertEquals(ItemCounts(0 of a, 0 of b), x * 0.0)
        assertEquals(zero, (x * 0.0).filerNonZero())
        assertEquals(ItemCounts(20 of a, 8.4 of b), x * 4.0)
        assertEquals(ItemCounts(2.5 of a, 1.05 of b), x / 2.0)
        assertThrows(IllegalArgumentException::class.java) { x / 0.0 }
    }

    @Test
    @DisplayName("Division test")
    fun divisionTest() {
        val zero = ItemCounts()
        val a = Item("a")
        val b = Item("b")
        val c = Item("c")

        val x = ItemCounts(1.2 of a, 3.6 of b)
        val y = ItemCounts(4.5 of a, 6.7 of c)
        val z = ItemCounts(4.5 of a)
        assertEquals(1.0, x / x)
        assertEquals(0.0, zero / x)
        assertEquals(Double.POSITIVE_INFINITY, x / zero)
        assertEquals(Double.POSITIVE_INFINITY, zero / zero)
        assertEquals(0.0, x / y)
        assertEquals(1.2 / 4.5, x / z)
    }

    @Test
    @DisplayName("Ceiling test")
    fun ceilTest() {
        val zero = ItemCounts()
        val a = Item("a")
        val b = Item("b")
        val c = Item("c")

        val x = ItemCounts(1.2 of a, 3.6 of b, 5 of c)
        val y = ItemCounts(2.5 of a, 3.1 of b)
        val z = ItemCounts(1.2 of a, 3.1 of b)
        val d = ItemCounts(3 of c)

        assertEquals(x, x.ceilWith(x))
        assertEquals(z + ItemCounts(0 of c), x.ceilWith(y))
        assertEquals(z, y.ceilWith(x))

        assertEquals(zero, z.ceilWith(zero).filerNonZero())
        assertEquals(zero, z.ceilWith(d).filerNonZero())
        assertEquals(zero, d.ceilWith(z).filerNonZero())
    }

    @Test
    @DisplayName("toString test")
    fun toStringTest() {
        val a = Item("a")

        assertEquals("ItemCounts{}", ItemCounts().toString())
        assertEquals("ItemCounts{a: 2.0}", ItemCounts(2 of a).toString())
    }
}