package net.ldvsoft.calcutorio.model.storage

import net.ldvsoft.calcutorio.model.base.Identified

interface IdMap<out T : Identified> : Set<T> {
    operator fun contains(id: String): Boolean
    operator fun get(id: String): T?
    fun getOrDefault(id: String, default: @UnsafeVariance T): T
}

fun <T : Identified> Iterable<T>.mapping() = map { it -> Pair(it.id, it) }.toMap()

interface MutableIdMap<T : Identified> : MutableSet<T>, IdMap<T> {
    fun remove(id: String): Boolean
}