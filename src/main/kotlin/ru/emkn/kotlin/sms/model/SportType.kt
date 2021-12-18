package ru.emkn.kotlin.sms.model


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

        fun getSportType(value: String) = sportTypeMap[value.lowercase()] ?: X
    }
}