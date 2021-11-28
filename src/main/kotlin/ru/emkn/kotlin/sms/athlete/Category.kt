package ru.emkn.kotlin.sms.athlete

enum class Category {
    NONE, III_JUNIOR, II_JUNIOR, I_JUNIOR, III, II, I, CANDIDATE, MASTER, INTERNATIONAL_MASTER, X;

    override fun toString() = toRussian[this]!!

    companion object {
        private val categoryMap = setOf("", "нет", "никакой", "no", "none", "na").associateWith { NONE }.toMutableMap()
            .apply { putAll(setOf("IIIю", "3ю", "3 юношеский", "third junior").associateWith { III_JUNIOR }) }
            .apply { putAll(setOf("IIю", "2ю", "2 юношеский", "second junior").associateWith { II_JUNIOR }) }
            .apply { putAll(setOf("Iю", "1ю", "1 юношеский", "first junior").associateWith { I_JUNIOR }) }
            .apply { putAll(setOf("III", "3", "3 взрослый", "third").associateWith { III_JUNIOR }) }
            .apply { putAll(setOf("II", "2", "2 взрослый", "second").associateWith { III_JUNIOR }) }
            .apply { putAll(setOf("I", "1", "1 взрослый", "first").associateWith { III_JUNIOR }) }
            .apply { putAll(setOf("КМС", "кандидат в мастера спорта", "candidate").associateWith { CANDIDATE }) }
            .apply { putAll(setOf("МС", "мастера спорт", "master").associateWith { MASTER }) }
            .apply { putAll(setOf("ММС", "международный мастер").associateWith { INTERNATIONAL_MASTER }) }.toMap()


        private val toRussian = mapOf(
            I_JUNIOR to "Iю", II_JUNIOR to "IIю", III_JUNIOR to "IIIю",
            I to "I", II to "II", III to "III",
            CANDIDATE to "КМС", MASTER to "МС", INTERNATIONAL_MASTER to "ММС",
            X to "эксепшн кидать или это unexpected behaviour у toString будет? прив как дела"
        )
        //дает разряд по строке
        fun getCategory(str: String): Category {
            if (categoryMap.containsKey(str)) {
                return categoryMap[str]!!
            }
            return X
        }
    }
}