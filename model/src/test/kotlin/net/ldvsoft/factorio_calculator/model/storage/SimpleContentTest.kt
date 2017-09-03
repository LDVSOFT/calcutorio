package net.ldvsoft.factorio_calculator.model.storage

import net.ldvsoft.factorio_calculator.model.base.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Duration

@DisplayName("Simple Content tests")
internal class SimpleContentTest {
    @Test
    @DisplayName("Simple test")
    fun simpleTest() {
        with(SimpleContent()) {

            assertTrue(items.isEmpty())
            assertTrue(machineTypes.isEmpty())
            assertTrue(machines.isEmpty())
            assertTrue(recipes.isEmpty())

            recipes.add(Recipe("r1",
                    ItemCounts(2 of Item("Iron"), 1 of Item("Coal")), ItemCounts(3 of Item("Iron plate")),
                    Duration.ofSeconds(2), mapOf(MachineType("furnace") to 0)
            ))
            machines.add(Machine("uber-furnace",
                    MachineType("furnace"), 1000, 20.0
            ))
            items.add(Item("Diamond"))

            assertTrue(items.containsAll(listOf("Iron", "Coal", "Iron plate").map(::Item)))
            assertEquals(4, items.size)
            assertTrue(machineTypes.contains(MachineType("furnace")))
            assertEquals(1, machineTypes.size)
            assertTrue(machines.contains("uber-furnace"))
            assertEquals(1, machines.size)
            assertTrue(recipes.contains("r1"))
            assertEquals(1, recipes.size)

            assertThrows(IllegalStateException::class.java) { items.remove("Coal") }
            assertThrows(IllegalStateException::class.java) { items.remove("Iron plate") }
            assertTrue(items.remove("Diamond"))

            assertThrows(IllegalStateException::class.java) { machineTypes.remove("furnace") }
            assertFalse(machineTypes.remove("hands"))

            assertTrue(machines.remove("uber-furnace"))
            assertThrows(IllegalStateException::class.java) { machineTypes.remove("furnace") }
            assertThrows(IllegalStateException::class.java) { machineTypes.clear() }

            assertTrue(recipes.remove("r1"))
            assertTrue(machineTypes.remove("furnace"))
        }
    }
}