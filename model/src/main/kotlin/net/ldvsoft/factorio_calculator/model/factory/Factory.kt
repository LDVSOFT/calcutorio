package net.ldvsoft.factorio_calculator.model.factory

class Factory {
    private val internalNodes: MutableCollection<FactoryNode> = mutableSetOf()

    val nodes: Collection<FactoryNode>
        get() = internalNodes

    fun addNode(node: FactoryNode): Boolean {
        if (node.isLinked())
            throw IllegalStateException("Node to be added is already linked")
        return internalNodes.add(node)
    }

    fun removeNode(node: FactoryNode): Boolean {
        if (node.isLinked())
            throw IllegalStateException("Node to be removed is still linked")
        return internalNodes.remove(node)
    }

    fun link(from: FactoryPort, to: FactoryPort) {
        if (from.owner !in internalNodes || to.owner !in internalNodes)
            throw IllegalArgumentException("Node(s) is not from this factory")
        from.linkTo(to)
    }

    fun unlink(from: FactoryPort, to: FactoryPort) {
        if (from.owner !in internalNodes || to.owner !in internalNodes)
            throw IllegalArgumentException("Node(s) is not from this factory")
        if (from.linkedPort != to)
            throw IllegalArgumentException("Nodes are not linked")
        from.unlink()
    }

    fun tick() {
        nodes.forEach(FactoryNode::tick)
        nodes.forEach(FactoryNode::postTick)
    }
}