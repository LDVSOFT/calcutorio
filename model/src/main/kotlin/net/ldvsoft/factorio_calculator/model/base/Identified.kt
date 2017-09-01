package net.ldvsoft.factorio_calculator.model.base

interface Identified {
    val id: String
    val idType: String
}

abstract class IdentifiedObject(override val idType: String): Identified