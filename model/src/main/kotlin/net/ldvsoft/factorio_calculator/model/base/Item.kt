package net.ldvsoft.factorio_calculator.model.base

import com.fasterxml.jackson.annotation.JsonValue

data class Item constructor(@get:JsonValue override val id: String) : IdentifiedObject("item")