package net.ldvsoft.factorio_calculator.model.base

infix fun Int.of(item: Item) = item to toDouble()
infix fun Double.of(item: Item) = item to this

class ItemCounts(val counts: Map<Item, Double> = emptyMap()): Map<Item, Double> by counts {
    constructor(vararg counts: Pair<Item, Double>): this(mapOf(*counts))

    override operator fun get(key: Item): Double {
        return counts.getOrDefault(key, .0)
    }

    operator fun plus(that: ItemCounts): ItemCounts {
        val resultMap = counts.toMutableMap()
        that.forEach { item, d -> resultMap[item] = resultMap.getOrDefault(item, .0) + d }
        return ItemCounts(resultMap)
    }

    operator fun minus(that: ItemCounts): ItemCounts {
        val resultMap = counts.toMutableMap()
        that.forEach { item, d -> resultMap[item] = resultMap.getOrDefault(item, .0) - d }
        return ItemCounts(resultMap)
    }

    operator fun unaryMinus(): ItemCounts {
        return ItemCounts(counts.mapValues { (_, it) -> -it })
    }

    operator fun times(k: Double): ItemCounts {
        return ItemCounts(counts.mapValues { (_, it) -> it * k })
    }

    operator fun div(k: Double): ItemCounts {
        return times(1 / k)
    }

    operator fun div(that: ItemCounts): Double {
        return that.map { (item, cnt) -> this[item] / cnt }.min() ?: Double.POSITIVE_INFINITY
    }

    fun notGreaterThan(that: ItemCounts) = all { (item, value) -> value <= that[item] }

    inline fun forEach(body: (Item, Double) -> Unit) {
        counts.entries.forEach { (item, cnt) -> body(item, cnt)}
    }

    fun filerNonZero(): ItemCounts {
        return ItemCounts(counts.filterValues { it != .0 })
    }

    fun ceilWith(ceil: ItemCounts): ItemCounts {
        return ItemCounts(ceil.mapValues { (item, cnt) -> maxOf(cnt, this[item]) })
    }

    operator fun contains(item: Item) = keys.contains(item)

    override fun toString(): String {
        return entries.joinToString(prefix = "ItemCounts{", postfix = "}") { "${it.key.id}: ${it.value}" }
    }
}