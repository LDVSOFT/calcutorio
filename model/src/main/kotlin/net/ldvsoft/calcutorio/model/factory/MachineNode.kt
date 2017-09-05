package net.ldvsoft.calcutorio.model.factory

import net.ldvsoft.calcutorio.model.base.ItemCounts
import net.ldvsoft.calcutorio.model.base.Machine
import net.ldvsoft.calcutorio.model.base.Recipe

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