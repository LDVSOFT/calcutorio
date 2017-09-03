package net.ldvsoft.factorio_calculator.model.base

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Identified object tests")
internal class IdentifiedTest {
    /**
     * This is simple test to check how equality and compareTo works.
     */
    @Test
    @DisplayName("Identified objects test")
    fun identifiedTest() {
        val a = Item("a")
        val otherA = Item("a")
        val b = Item("b")

        val c = MachineType("a")

        assertEquals(a, a)
        assertEquals(a, otherA)
        assertNotEquals(a, b)
        assertNotEquals(a, c)

        assertTrue(a < b)
        assertTrue(b > a)
        assertTrue(! (a < otherA || a > otherA))
        assertThrows(IllegalArgumentException::class.java) { a < c }
    }
}