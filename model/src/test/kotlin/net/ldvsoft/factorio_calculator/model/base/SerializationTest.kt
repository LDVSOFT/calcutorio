package net.ldvsoft.factorio_calculator.model.base

import net.ldvsoft.factorio_calculator.utils.SerializationTestBase
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Duration

@DisplayName("Serialization tests")
internal class SerializationTest : SerializationTestBase() {
    @Test
    @DisplayName("Item and Machine serialization test")
    fun itemAndMachineTypeTest() {
        listOf("a", "Crazzy stuff", "bcd-eeefhsj").forEach { name ->
            val wrappedName = "\"$name\""

            assertSerializationWorks(Item(name), wrappedName)
            assertSerializationWorks(MachineType(name), wrappedName)
        }
    }

    @Test
    @DisplayName("ItemCounts serialization test")
    fun itemCountsTest() {
        val a = Item("a")
        val b = Item("b")
        val c = Item("c")
        val counts = ItemCounts(1 of a, 2.1 of b, 3.2 of c)
        val countsRepr = """{
                "a":1.0,
                "b":2.1,
                "c":3.2
            }""".trimIndent()

        assertSerializationWorks(counts, countsRepr)
    }

    @Test
    @DisplayName("Recipe serialization test")
    fun recipeTest() {
        val a = Item("a")
        val b = Item("b")
        val c = Item("c")

        val assembler = MachineType("assembling-machine")
        val hands = MachineType("hands")

        val recipe = Recipe("r1",
                ItemCounts(1 of a, 2 of b), ItemCounts(3 of c),
                Duration.ofSeconds(1), mapOf(assembler to 1, hands to 0))
        val recipeRepr = """{
                "id":"r1",
                "ingredients":{"a":1.0,"b":2.0},
                "products":{"c":3.0},
                "timeToCraft":1.0,
                "requiredMachineLevel":{"assembling-machine":1,"hands":0}
        }""".trimIndent()

        assertSerializationWorks(recipe, recipeRepr)
    }

    @Test
    @DisplayName("Machine serialization test")
    fun machineTest() {
        val m = MachineType("m")

        val machine = Machine("m0", m, 0, 1.0)
        val machineRepr = """{
            "id": "m0",
            "type": "m",
            "level": 0,
            "speed": 1.0
        }""".trimIndent()

        assertSerializationWorks(machine, machineRepr)
    }
}