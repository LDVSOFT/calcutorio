package net.ldvsoft.factorio_calculator.model.base

import net.ldvsoft.factorio_calculator.utils.div
import java.time.Duration

data class Recipe(
        override val id: String,
        val ingredients: ItemCounts,
        val products: ItemCounts,
        val timeToCraft: Duration,
        val requiredMachineLevel: Map<MachineType, Int>
): IdentifiedObject("recipe") {
    fun canBeCraftedIn(machine: Machine): Boolean {
        val requiredLevel = requiredMachineLevel[machine.type] ?: return false
        return requiredLevel >= machine.level
    }

    fun getOutputWith(machine: Machine, input: ItemCounts): ItemCounts {
        if (!canBeCraftedIn(machine))
            throw IllegalArgumentException("Machine $machine cannot craft $this")
        val repeats = input / ingredients
        return products * repeats
    }

    fun getMaximumOutputFlowWith(machine: Machine, per: Duration): ItemCounts {
        if (!canBeCraftedIn(machine))
            throw IllegalArgumentException("Machine $machine cannot craft $this")
        return products * (per / timeToCraft / machine.speed)
    }
}