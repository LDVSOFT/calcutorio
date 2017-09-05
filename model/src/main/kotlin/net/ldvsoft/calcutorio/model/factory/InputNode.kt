package net.ldvsoft.calcutorio.model.factory

import net.ldvsoft.calcutorio.model.base.ItemCounts

class InputNode(providedFlow: ItemCounts) : FactoryNode(0, 1) {
    init {
        with(outputs[0]) {
            maxFlow = providedFlow
        }
    }

    override fun tickMaximumFlow() {}
    override fun tickCurrentFlow() {}
}