package net.ldvsoft.factorio_calculator.model.storage

import net.ldvsoft.factorio_calculator.model.base.IdentifiedObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Simple IdMap tests")
internal class SimpleIdMapTest {
    class Object(override val id: String): IdentifiedObject("test") {
        override fun toString(): String {
            return "Object(id='$id')"
        }
    }

    val a = Object("a")
    val b = Object("b")
    val c = Object("c")
    val d = Object("d")

    @Test
    @DisplayName("Addition and removal test")
    fun additionTest() {
        val map: MutableIdMap<Object> = SimpleIdMap()
        val otherA = Object("a")

        assertTrue(map.isEmpty())
        assertEquals(0, map.size)

        assertTrue(map.add(a))
        assertTrue(a in map)
        assertTrue("a" in map)
        assertTrue(b !in map)
        assertTrue("b" !in map)
        assertFalse(map.add(a))
        assertEquals(a, map["a"])
        assertFalse(map.isEmpty())
        assertEquals(1, map.size)

        assertEquals(a, map.getOrDefault("a", otherA))
        assertEquals(otherA, map.getOrDefault("b", otherA))

        assertThrows(IllegalStateException::class.java) { map.add(otherA) }
        assertThrows(IllegalStateException::class.java) { otherA in map }
        assertThrows(IllegalStateException::class.java) { map.remove(otherA) }

        assertTrue(map.add(b))
        assertTrue(a in map)
        assertTrue(b in map)
        assertFalse(map.add(a))
        assertFalse(map.add(b))

        assertTrue(map.remove(a))
        assertTrue(a !in map)
        assertTrue(b in map)
        assertFalse(map.remove(a))
        assertFalse(map.add(b))

        assertTrue(map.remove("b"))
        assertTrue(a !in map)
        assertTrue(b !in map)
        assertFalse(map.remove("a"))
        assertFalse(map.remove(b))
    }

    @Test
    @DisplayName("Listener test")
    fun listenerTest() {
        val events = mutableListOf<String>()
        val listener = object : SimpleIdMap.Listener<Object>() {
            override fun onAdd(element: Object) {
                if (element == b)
                    throw IllegalArgumentException("Do not add this element!")
                events.add("add ${element.id}")
            }

            override fun onRemove(element: Object) {
                events.add("rem ${element.id}")
            }
        }

        val map = SimpleIdMap(listener = listener)
        assertTrue(map.add(a))
        assertThrows(IllegalArgumentException::class.java) { map.add(b) }

        assertFalse(map.remove(b))
        assertTrue(map.remove(a))

        assertEquals(listOf("add a", "rem a"), events)
    }

    @Test
    @DisplayName("Massive operations test")
    fun massiveOperationsTest() {
        val map = SimpleIdMap<Object>()
        assertFalse(map.addAll(listOf()))
        assertTrue(map.isEmpty())

        assertTrue(map.addAll(listOf(a, b)))
        assertEquals(2, map.size)
        assertFalse(map.containsAll(listOf(b, c)))

        assertTrue(map.removeAll(listOf(a, c, d)))
        assertEquals(setOf(b), map.toSet())
        assertFalse(map.removeAll(listOf(a, c)))

        assertTrue(map.addAll(listOf(a, c)))
        assertEquals(3, map.size)
        assertTrue(map.containsAll(listOf(b, c)))
        assertEquals(setOf(a, b, c), map.toSet())
        assertFalse(map.retainAll(listOf(a, b, c)))

        assertTrue(map.retainAll(listOf(b, c, d)))
        assertEquals(setOf(b, c), map.toSet())

        map.clear()
        assertTrue(map.isEmpty())
        assertEquals(setOf<Object>(), map.toSet())
    }
}