package ru.emkn.kotlin.sms.athlete

enum class Sex {
    MALE, FEMALE, X;

    override fun toString() = toRussian[this]!!

    companion object {
        private val maleDescriptions = setOf("m", "M", "male", "man", "м", "М", "муж", "мужской", "мужчина")
        private val femaleDescriptions = setOf("w", "W", "female", "woman", "ж", "Ж", "жен", "женский", "женщина")

        //Карта, которая по описанию пола возвращает пол (см. выше)
        private val sexMap = buildSexMap(maleDescriptions, femaleDescriptions)

        private val toRussian = mapOf(
            MALE to "М", FEMALE to "Ж",
            X to "LBGTQ+ ну а вообще надо реально придумать что выводить в этом случае"
        )

        private fun buildSexMap(male: Set<String>, female: Set<String>): Map<String, Sex> {
            val result = maleDescriptions.associateWith { MALE }.toMutableMap()
            result.putAll(femaleDescriptions.associateWith { FEMALE })
            return result
        }

        //Возвращает пол, если пол указан корректно, и X иначе
        fun getSex(value: String): Sex = sexMap[value] ?: X

    }
}