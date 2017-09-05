package net.ldvsoft.calcutorio.model.base

import com.fasterxml.jackson.annotation.JsonIgnore

interface Identified : Comparable<Identified> {
    val id: String
    @get:JsonIgnore
    val idType: String

    override fun compareTo(other: Identified): Int {
        if (idType != other.idType)
            throw IllegalArgumentException("Cannot compare Identified of different type")
        return id.compareTo(other.id)
    }
}

abstract class IdentifiedObject(override val idType: String) : Identified