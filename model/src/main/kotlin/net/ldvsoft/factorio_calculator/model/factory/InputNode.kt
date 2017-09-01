package net.ldvsoft.factorio_calculator.model.factory

import net.ldvsoft.factorio_calculator.model.base.ItemCounts

class InputNode(providedFlow: ItemCounts) : FactoryNode(0, 1) {
    init {
        with(outputs[0]) {
            maxFlow = providedFlow
        }
    }

    override fun tickMaximumFlow() {}
    override fun tickCurrentFlow() {}
}