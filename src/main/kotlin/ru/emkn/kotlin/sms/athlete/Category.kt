package ru.emkn.kotlin.sms.athlete

enum class Category {
    NONE, III_JUNIOR, II_JUNIOR, I_JUNIOR, III, II, I, CANDIDATE, MASTER, INTERNATIONAL_MASTER, X;

    override fun toString() = TODO()

    companion object {
        //TODO()
        fun getCategory(str: String): Category {
            return I
        }
    }
}