package ru.emkn.kotlin.sms.model

import kotlin.String


enum class SportType() {
    RUNNING, SWIMMING, ORIENTEERING, X;

    fun toRussian() = when (this) {
        ORIENTEERING -> "ориентирование"
        RUNNING -> "бег"
        SWIMMING -> "плавание"
        X -> "X"
    }

    companion object {
        private val sportTypeMap = setOf("бег", "running").associateWith { RUNNING }.toMutableMap()
            .apply { putAll(setOf("плавание", "бультых", "swimming").associateWith { SWIMMING }) }.toMutableMap()
            .apply {
                putAll(
                    setOf(
                        "спортивное ориентирование",
                        "ориентирование",
                        "ориенташка",
                        "orienteering"
                    ).associateWith { ORIENTEERING })
            }.toMap()

        fun get(value: String) = sportTypeMap[value.lowercase()] ?: X
    }
}