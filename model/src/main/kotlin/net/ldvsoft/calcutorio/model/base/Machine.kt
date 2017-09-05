package net.ldvsoft.calcutorio.model.base

data class Machine(
        override val id: String,
        val type: MachineType,
        val level: Int,
        val speed: Double
) : IdentifiedObject("machine")