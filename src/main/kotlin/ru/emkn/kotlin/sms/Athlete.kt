package ru.emkn.kotlin.sms

import kotlinx.datetime.*

class AthleteNumber(val value: String) {
}

enum class Sex {
    MALE, FEMALE
}

class Name(val firstName: String, val secondName: String) {
    override fun toString(): String {
        TODO()
    }
}

class Category(val categoryName: String) {
}

class Insurance(val exist: Boolean = false) {
}

class MedicalCondition(val canParticipate: Boolean = false) {
}

class Athlete(
    val number: AthleteNumber,
    private val name: Name,
    private val sex: Sex,
    private val birthDate: LocalDate,
    private val sportCategory: Category,
    private val insurance: Insurance,
    private val medicalCondition: MedicalCondition,
    private val team: Team
) {
    private val group: Group = TODO() // в какой момент мы им это присваиваем? или может это не должно быть?
}


