package net.ldvsoft.factorio_calculator.model.base

class ItemCounts(val counts: Map<Item, Double> = emptyMap()): Map<Item, Double> by counts {
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
        return that.minBy { (item, cnt) -> this[item] / cnt }?.component2() ?: Double.POSITIVE_INFINITY
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

    fun has(item: Item) = keys.contains(item)
}