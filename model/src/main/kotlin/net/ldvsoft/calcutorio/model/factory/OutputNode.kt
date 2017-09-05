package net.ldvsoft.calcutorio.model.factory

class OutputNode : FactoryNode(1, 0) {
    override fun tickMaximumFlow() {
        with(inputs[0]) {
            curFlow = maxFlow
        }
    }

    override fun tickCurrentFlow() {}
}