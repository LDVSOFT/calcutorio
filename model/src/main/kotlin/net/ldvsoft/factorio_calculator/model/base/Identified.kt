package net.ldvsoft.factorio_calculator.model.base

interface Identified: Comparable<Identified> {
    val id: String
    val idType: String

    override fun compareTo(other: Identified): Int {
        if (idType != other.idType)
            throw IllegalArgumentException("Cannot compare Identified of different type")
        return id.compareTo(other.id)
    }
}

abstract class IdentifiedObject(override val idType: String): Identified