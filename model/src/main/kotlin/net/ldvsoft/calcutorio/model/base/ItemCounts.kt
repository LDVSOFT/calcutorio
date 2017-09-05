package net.ldvsoft.calcutorio.model.base

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef

infix fun Int.of(item: Item) = item to toDouble()
infix fun Double.of(item: Item) = item to this

private typealias RawItemCounts = Map<Item, Double>

@JsonDeserialize(using = ItemCountsDeserializer::class)
data class ItemCounts(val counts: Map<Item, Double> = emptyMap()) : Map<Item, Double> by counts {
    constructor(vararg counts: Pair<Item, Double>) : this(mapOf(*counts))

    override operator fun get(key: Item): Double {
        return counts.getOrDefault(key, .0)
    }

    operator fun plus(that: ItemCounts): ItemCounts {
        val resultMap = counts.toMutableMap()
        that.forEach { (item, d) -> resultMap[item] = resultMap.getOrDefault(item, .0) + d }
        return ItemCounts(resultMap)
    }

    operator fun minus(that: ItemCounts): ItemCounts {
        val resultMap = counts.toMutableMap()
        that.forEach { (item, d) -> resultMap[item] = resultMap.getOrDefault(item, .0) - d }
        return ItemCounts(resultMap)
    }

    operator fun unaryMinus(): ItemCounts {
        return ItemCounts(counts.mapValues { (_, it) -> -it })
    }

    operator fun times(k: Double): ItemCounts {
        return ItemCounts(counts.mapValues { (_, it) -> it * k })
    }

    operator fun div(k: Double): ItemCounts {
        if (k == 0.0)
            throw IllegalArgumentException("Division by zero")
        return times(1 / k)
    }

    operator fun div(that: ItemCounts): Double {
        return that.map { (item, cnt) -> this[item] / cnt }.min() ?: Double.POSITIVE_INFINITY
    }

    fun filerNonZero(): ItemCounts {
        return ItemCounts(counts.filterValues { it != .0 })
    }

    fun ceilWith(ceil: ItemCounts): ItemCounts {
        return ItemCounts(mapValues { (item, cnt) -> minOf(cnt, ceil[item]) })
    }

    operator fun contains(item: Item) = keys.contains(item)

    override fun toString(): String {
        return entries.joinToString(prefix = "ItemCounts{", postfix = "}") { "${it.key.id}: ${it.value}" }
    }
}

internal object ItemCountsDeserializer: JsonDeserializer<ItemCounts>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ItemCounts {
        /**
         * Because of Kotlin immutable containers, we need to wrap deserialization.
         * Else, we cannot deserialize ItemCount
         */
        val underlyingTypeRef = jacksonTypeRef<RawItemCounts>()
        val underlyingJavaType = TypeFactory.defaultInstance().constructType(underlyingTypeRef)
        val underlyingMapper = @Suppress("UNCHECKED_CAST") (
                ctxt.findRootValueDeserializer(underlyingJavaType) as JsonDeserializer<out RawItemCounts>
                )
        val map = underlyingMapper.deserialize(p, ctxt)
        return ItemCounts(map)
    }
}