package net.ldvsoft.factorio_calculator.model

import net.ldvsoft.factorio_calculator.model.base.Identified
import net.ldvsoft.factorio_calculator.model.storage.MutableIdMap
import net.ldvsoft.factorio_calculator.model.storage.mapping

class SimpleIdMap<T: Identified> internal constructor(data: Iterable<T> = emptyList(), val listener: Listener<T>?): MutableIdMap<T> {
    private val map: MutableMap<String, T> = data.mapping().toMutableMap()

    internal abstract class Listener<T> {
        open fun onAdd(element: T) {}
        open fun onRemove(element: T) {}
    }

    constructor(data: Iterable<T> = emptyList()): this(data, null)

    override fun contains(id: String) = map.containsKey(id)
    override fun get(id: String) = map[id]
    override fun getOrDefault(id: String, default: T) = map.getOrDefault(id, default)
    override val size = map.size
    override fun isEmpty() = map.isEmpty()

    override fun add(element: T): Boolean {
        val storedElement = map[element.id]
        if (storedElement == null) {
            listener?.onAdd(element)
            map.put(element.id, element)
            return true
        }
        if (storedElement != element)
            throw IllegalStateException("Two different objects with same id")
        return false
    }

    override fun contains(element: T): Boolean {
        val storedElement = map[element.id] ?: return false
        if (storedElement != element)
            throw IllegalStateException("Two different objects with same id")
        return true
    }

    override fun remove(element: T): Boolean {
        val storedElement = map[element.id] ?: return false
        if (storedElement != element)
            throw IllegalStateException("Two different objects with same id")
        listener?.onRemove(element)
        map.remove(element.id)
        return true
    }

    override fun addAll(elements: Collection<T>) = elements.any { add(it) }
    override fun containsAll(elements: Collection<T>) = elements.any { contains(it) }
    override fun removeAll(elements: Collection<T>): Boolean = elements.any { remove(it) }

    override fun clear() = map.clear()
    override fun remove(id: String) = map.remove(id) != null
    override fun iterator() = map.values.iterator()

    override fun retainAll(elements: Collection<T>): Boolean {
        var changed: Boolean = false
        val iterator = iterator()
        while (iterator.hasNext()) {
            val it = iterator.next()
            if (it !in elements) {
                iterator.remove()
                changed = true
            }
        }
        return changed
    }
}