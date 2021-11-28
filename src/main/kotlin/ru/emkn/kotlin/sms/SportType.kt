package ru.emkn.kotlin.sms


enum class SportType() {
    RUNNING, SWIMMING, X;

    override fun toString(): String = toRussian[this]!!


    companion object {
        private val toRussian = mapOf(RUNNING to "RUNNING", SWIMMING to "плавание", X to "X")

                private val sportTypeMap = setOf("бег", "running").associateWith { RUNNING }.toMutableMap()
            .apply { putAll(setOf("плавание", "бультых", "swimming").associateWith { SWIMMING }) }.toMap()

            fun getSportType(value: String) = sportTypeMap[value] ?: X

    }
}