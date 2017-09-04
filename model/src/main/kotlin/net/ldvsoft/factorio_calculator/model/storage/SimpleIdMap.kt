package net.ldvsoft.factorio_calculator.model.storage

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import net.ldvsoft.factorio_calculator.model.base.Identified
import net.ldvsoft.factorio_calculator.utils.fullAll
import net.ldvsoft.factorio_calculator.utils.fullAny

class SimpleIdMap<T : Identified> internal constructor(
        data: Collection<T> = emptyList(),
        @get:JsonIgnore
        val listener: Listener<T>?
) : MutableIdMap<T> {
    private val map: MutableMap<String, T> = data.mapping().toMutableMap()

    internal abstract class Listener<in T> {
        open fun onAdd(element: T) {}
        open fun onRemove(element: T) {}
    }

    internal constructor(vararg elements: T, listener: Listener<T>): this(listOf(*elements), listener)

    @JsonCreator
    constructor(vararg elements: T) : this(listOf(*elements), null)

    override operator fun contains(id: String) = map.containsKey(id)
    override operator fun get(id: String) = map[id]
    override fun getOrDefault(id: String, default: T) = map.getOrDefault(id, default)
    override val size get() = map.size
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

    override fun remove(id: String): Boolean {
        val storedElement = map[id] ?: return false
        listener?.onRemove(storedElement)
        map.remove(id)
        return true
    }

    override fun addAll(elements: Collection<T>) = elements.fullAny { add(it) }
    override fun containsAll(elements: Collection<T>) = elements.fullAll { contains(it) }
    override fun removeAll(elements: Collection<T>): Boolean = elements.fullAny { remove(it) }

    override fun iterator(): MutableIterator<T> {
        val realIterator = map.values.iterator()
        return object: MutableIterator<T> by realIterator {
            private lateinit var lastGiven: T

            override fun next(): T {
                lastGiven = realIterator.next()
                return lastGiven
            }

            override fun remove() {
                listener?.onRemove(lastGiven)
                realIterator.remove()
            }
        }
    }

    override fun clear() {
        retainAll { false }
    }

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

    override fun equals(other: Any?): Boolean {
        if (other !is IdMap<*>)
            return false
        return other.toSet() == map.values.toSet()
    }

    override fun hashCode(): Int {
        return map.hashCode()
    }

    override fun toString(): String {
        if (listener == null)
            return map.toString()
        else
            return "SimpleIdMap(listener=$listener)$map"
    }
}