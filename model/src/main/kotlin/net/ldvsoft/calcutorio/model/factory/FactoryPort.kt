package net.ldvsoft.calcutorio.model.factory

import net.ldvsoft.calcutorio.model.base.ItemCounts

class FactoryPort internal constructor(val owner: FactoryNode, val isOutputPort: Boolean) {
    private var maxFlowChanged = false
    private var curFlowChanged = false

    var maxFlow: ItemCounts = ItemCounts()
        internal set(value) {
            field = value
            curFlow = curFlow
            if (isOutputPort)
                maxFlowChanged = true
            else
                owner.inputChanged = true
        }

    var curFlow: ItemCounts = ItemCounts()
        internal set(value) {
            field = value.ceilWith(maxFlow)
            if (isOutputPort)
                owner.outputChanged = true
            else
                curFlowChanged = true
        }

    var linkedPort: FactoryPort? = null
        private set
    var linked = linkedPort != null

    fun linkTo(target: FactoryPort) {
        if (linked || target.linked)
            throw IllegalArgumentException("One of the ports is already linked")
        if (!isOutputPort || target.isOutputPort)
            throw IllegalArgumentException("Wrong link direction")
        linkedPort = target
        target.linkedPort = this
    }

    fun unlink() {
        if (linkedPort == null)
            return
        val oldLinkedNode = linkedPort!!
        linkedPort = null
        oldLinkedNode.unlink()
    }

    internal fun tick() {
        if (maxFlowChanged) {
            linkedPort?.maxFlow = maxFlow
            maxFlowChanged = false
        }
        if (curFlowChanged) {
            linkedPort?.curFlow = curFlow
            curFlowChanged = false
        }
    }
}