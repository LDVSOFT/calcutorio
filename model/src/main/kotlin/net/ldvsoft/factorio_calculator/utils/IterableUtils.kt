package net.ldvsoft.factorio_calculator.utils

inline fun <T> Iterable<T>.fullAny(predicate: (T) -> Boolean): Boolean {
    var anyMathed = false
    forEach { if (predicate(it)) anyMathed = true }
    return anyMathed
}

inline fun <T> Iterable<T>.fullAll(predicate: (T) -> Boolean): Boolean {
    var allMatched = true
    forEach { if (!predicate(it)) allMatched = false }
    return allMatched
}