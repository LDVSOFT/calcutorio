package net.ldvsoft.factorio_calculator.model.storage

import net.ldvsoft.factorio_calculator.model.base.Identified

interface IdMap<T: Identified>: Collection<T> {
    operator fun contains(id: String): Boolean
    fun get(id: String): T?
    fun getOrDefault(id: String, default: T): T
}

fun <T: Identified> Iterable<T>.mapping() = map { it -> Pair(it.id, it) }.toMap()

interface MutableIdMap<T: Identified>: MutableCollection<T>, IdMap<T> {
    fun remove(id: String): Boolean
}