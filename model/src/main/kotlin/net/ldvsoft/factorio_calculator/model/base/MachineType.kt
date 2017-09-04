package net.ldvsoft.factorio_calculator.model.base

import com.fasterxml.jackson.annotation.JsonValue

data class MachineType(@get:JsonValue override val id: String) : IdentifiedObject("machineType")