package net.ldvsoft.factorio_calculator.model

import kotlin.reflect.KClass

/**
 * Created by ldvsoft on 03.08.17.
 */
interface Serialisation {
    fun <T: Any> serialize(obj: T): String
    fun <T: Any> deserialize(str: String, cls: KClass<T>): T
}

inline fun <reified T: Any> Serialisation.deserialize(str: String)
        = deserialize(str, T::class)