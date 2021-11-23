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

class Athlete(
    private val name: Name,
    private val sex: Sex,
    private val birthDate: LocalDate,
    private val sportCategory: Category,
    private val preferredGroup: GroupType = GroupType("")
) {
    val team: Team = TODO()
    val number: AthleteNumber = TODO()
    private val group: Group = TODO() // в какой момент мы им это присваиваем? или может это не должно быть?
}


