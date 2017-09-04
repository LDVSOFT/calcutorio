package net.ldvsoft.factorio_calculator.utils

import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.*

abstract class SerializationTestBase {
    protected val objectMapper = calcutorioObjectMapper()

    protected fun assertSerializesTo(expectedRepr: String, actualObject: Any) {
        val expectedTree = objectMapper.readTree(expectedRepr)
        val actualTree = objectMapper.readTree(objectMapper.writeValueAsString(actualObject))
        assertEquals(expectedTree, actualTree)
    }

    protected inline fun <reified T: Any> assertDeserializesTo(expectedObject: T, actualRepr: String) {
        val actualObject = objectMapper.readValue<T>(actualRepr)
        assertEquals(expectedObject, actualObject)
    }

    protected inline fun <reified T: Any> assertSerializationWorks(anObject: T, aRepresentaion: String) {
        assertSerializesTo(aRepresentaion, anObject)
        assertDeserializesTo(anObject, aRepresentaion)
    }
}

