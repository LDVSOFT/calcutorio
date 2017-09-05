package net.ldvsoft.calcutorio.model.factory

abstract class FactoryNode(inputPorts: Int = 0, outputPorts: Int = 0) {
    internal var inputChanged: Boolean = false
    internal var outputChanged: Boolean = false

    var inputs: List<FactoryPort> = ArrayList()
        private set
    var inputsCount: Int = 0
        protected set(newCount) = setPortCounts(newCount, false)

    var outputs: List<FactoryPort> = ArrayList()
        private set
    var outputCount: Int = 0
        protected set (newCount) = setPortCounts(newCount, true)

    init {
        this.inputsCount = inputPorts
        this.outputCount = outputPorts
    }

    private fun setPortCounts(newCount: Int, isOutputPorts: Boolean) {
        val oldPorts = if (isOutputPorts) outputs else inputs
        val oldCount = oldPorts.size
        val newPorts = when {
            newCount < oldCount -> {
                oldPorts.drop(newCount).forEach { it.unlink() }
                oldPorts.take(newCount)
            }
            newCount > oldCount -> {
                val newPorts = ArrayList<FactoryPort>()
                newPorts.addAll(oldPorts)
                (oldCount..newCount).forEach { newPorts.add(FactoryPort(this, isOutputPorts)) }
                newPorts
            }
            else -> oldPorts
        }
        if (isOutputPorts)
            outputs = newPorts
        else
            inputs = newPorts
    }

    internal fun tick() {
        if (inputChanged) {
            tickMaximumFlow()
            inputChanged = false
        }
        if (outputChanged) {
            tickCurrentFlow()
            outputChanged = false
        }
    }

    internal fun postTick() {
        inputs.forEach { it.tick() }
        outputs.forEach { it.tick() }
    }

    fun isLinkedTo(other: FactoryNode): Boolean {
        inputs.forEach { if (it.linkedPort?.owner == other) return true }
        inputs.forEach { if (it.linkedPort?.owner == other) return true }
        return false
    }

    fun isLinked(): Boolean {
        return inputs.any { it.linked } || outputs.any { it.linked }
    }

    internal abstract fun tickMaximumFlow()
    internal abstract fun tickCurrentFlow()
}