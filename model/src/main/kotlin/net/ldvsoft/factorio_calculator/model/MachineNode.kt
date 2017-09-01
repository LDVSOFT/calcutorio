package net.ldvsoft.factorio_calculator.model

import net.ldvsoft.factorio_calculator.model.base.FactoryNode
import net.ldvsoft.factorio_calculator.model.base.ItemCounts
import net.ldvsoft.factorio_calculator.model.base.Machine
import net.ldvsoft.factorio_calculator.model.base.Recipe

class MachineNode(val machine: Machine, recipe: Recipe) : FactoryNode(1, 1) {
    var recipe: Recipe = recipe
        set (newRecipe) {
            if (!newRecipe.canBeCraftedIn(machine))
                throw IllegalArgumentException("Recipe ${newRecipe.id} cannot be crafted in ${machine.id}")
            field = newRecipe
            output.maxFlow = ItemCounts()
        }

    private val output = outputs[0]
    private val input = inputs[0]

    override fun tickMaximumFlow() {
        output.maxFlow = recipe.getOutputWith(machine, input.maxFlow)
    }

    override fun tickCurrentFlow() {
        input.curFlow = input.maxFlow * (output.curFlow / output.maxFlow)
    }
}