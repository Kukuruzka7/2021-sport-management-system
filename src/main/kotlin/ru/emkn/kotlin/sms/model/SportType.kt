package ru.emkn.kotlin.sms.model

import kotlin.String


enum class SportType() {
    RUNNING, SWIMMING, X;

    fun toRussian() = when (this) {
        RUNNING -> "бег"
        SWIMMING -> "плавание"
        X -> "X"
    }

    companion object {
        private val sportTypeMap = setOf("бег", "running").associateWith { RUNNING }.toMutableMap()
            .apply { putAll(setOf("плавание", "бультых", "swimming").associateWith { SWIMMING }) }.toMap()

        fun get(value: String) = sportTypeMap[value.lowercase()] ?: X
    }
}