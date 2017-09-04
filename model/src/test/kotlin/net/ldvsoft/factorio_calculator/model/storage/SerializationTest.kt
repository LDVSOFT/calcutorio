package net.ldvsoft.factorio_calculator.model.storage

import net.ldvsoft.factorio_calculator.model.base.*
import net.ldvsoft.factorio_calculator.utils.SerializationTestBase
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Duration

@DisplayName("Serialization tests")
internal class SerializationTest : SerializationTestBase() {
    @Test
    @DisplayName("IdMap<Item> serialization test")
    fun idMapItemTest() {
        val a = Item("a")
        val b = Item("b")
        val c = Item("c")

        val map = SimpleIdMap(a, b, c)
        val mapWithListener = SimpleIdMap(a, b, c, listener = object : SimpleIdMap.Listener<Item>(){})
        val mapRepr = """["a","b","c"]"""
        assertSerializationWorks(map, mapRepr)
        assertSerializesTo(mapRepr, mapWithListener)
        assertDeserializesTo(map, objectMapper.writeValueAsString(mapWithListener))
    }

    @Test
    @DisplayName("IdMap<Machine> serialization test")
    fun idMapMachineTest() {
        val m = MachineType("m")
        val n = MachineType("n")

        val m0 = Machine("m0", m, 0, 1.0)
        val m1 = Machine("m1", m, 1, 1.2)
        val n0 = Machine("n0", n, 0, 1.5)

        val map = SimpleIdMap(m0, m1, n0)
        val mapRepr = """[
            {"id": "m0", "type": "m", "level": 0, "speed": 1.0},
            {"id": "m1", "type": "m", "level": 1, "speed": 1.2},
            {"id": "n0", "type": "n", "level": 0, "speed": 1.5}
        ]""".trimIndent()

        assertSerializationWorks(map, mapRepr)
    }

    @Test
    @DisplayName("Content serialization test")
    fun contentTest() {
        val content = SimpleContent()

        val a = Item("a")
        val b = Item("b")
        val c = Item("c")
        val d = Item("d")
        content.items.addAll(listOf(a, b, c, d))

        val m = MachineType("m")
        val n = MachineType("n")
        content.machineTypes.addAll(listOf(m, n))

        content.machines.add(Machine("m0", m, 0, 1.0))
        content.machines.add(Machine("m1", m, 1, 1.2))
        content.machines.add(Machine("n0", n, 0, 1.3))
        content.machines.add(Machine("n1", n, 1, 1.5))

        content.recipes.add(Recipe("r1",
                ItemCounts(5 of a), ItemCounts(1 of b),
                Duration.ofSeconds(1), mapOf(m to 0, n to 1)
        ))
        content.recipes.add(Recipe("r2",
                ItemCounts(2 of b), ItemCounts(10 of c, 1 of a),
                Duration.ofSeconds(3), mapOf(m to 1)
        ))
        content.recipes.add(Recipe("r3",
                ItemCounts(), ItemCounts(1 of d),
                Duration.ofSeconds(2), mapOf(n to 0)
        ))

        val contentRepr = """{
            "items": ["a","b","c","d"],
            "machineTypes": ["m","n"],
            "machines": [
                    {"id": "m0", "type": "m", "level": 0, "speed": 1.0},
                    {"id": "m1", "type": "m", "level": 1, "speed": 1.2},
                    {"id": "n0", "type": "n", "level": 0, "speed": 1.3},
                    {"id": "n1", "type": "n", "level": 1, "speed": 1.5}
                ],
            "recipes":[
                    {
                        "id": "r1", "ingredients": {"a": 5.0}, "products": {"b": 1.0},
                        "timeToCraft": 1.0, "requiredMachineLevel": {"m": 0, "n": 1}
                    },{
                        "id": "r2", "ingredients": {"b": 2.0}, "products": {"c": 10.0, "a": 1.0},
                        "timeToCraft":3.0, "requiredMachineLevel": {"m": 1}
                    },{
                        "id": "r3", "ingredients": {}, "products": {"d": 1.0},
                        "timeToCraft": 2.0, "requiredMachineLevel": {"n": 0}
                    }
                ]
        }""".trimIndent()

        assertSerializationWorks(content, contentRepr)
    }
}