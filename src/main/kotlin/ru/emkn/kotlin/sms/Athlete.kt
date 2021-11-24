package ru.emkn.kotlin.sms

import kotlinx.datetime.LocalDate

class AthleteNumber(val value: String) {

}

enum class Sex {
    MALE, FEMALE
}

class Name(val firstName: String, val secondName: String) {
    val fullName: String = TODO()
}

class Category(val categoryName: String) {
}

class Athlete(
    private val name: Name,
    private val sex: Sex,
    private val birthDate: LocalDate,
    private val sportCategory: Category,
    private val preferredGroup: GroupType = GroupType(""),
    val team: Team
) {
    val number: AthleteNumber = TODO()
    private val group: Group = TODO()
}


