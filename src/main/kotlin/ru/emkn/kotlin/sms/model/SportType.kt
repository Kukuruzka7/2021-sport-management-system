package ru.emkn.kotlin.sms.model

import kotlin.String


enum class SportType() {
    RUNNING, SWIMMING, ORIENTEERING, X;

    fun toRussian() = when (this) {
        ORIENTEERING -> "спортивное ориентирование"
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
                        "ориенташка",
                        "orienteering"
                    ).associateWith { ORIENTEERING })
            }.toMap()

        fun getSportType(value: String) = sportTypeMap[value.lowercase()] ?: X
    }
}