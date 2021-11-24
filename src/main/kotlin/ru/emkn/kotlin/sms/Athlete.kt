package ru.emkn.kotlin.sms

import kotlinx.datetime.LocalDate

class AthleteNumber(val value: String) {

}

enum class Sex {
    MALE, FEMALE, X
}

class Name(val firstName: String, val lastName: String) {
    val fullName: String = "$firstName $lastName"
}

class Category(val categoryName: String) {
}

class Athlete(
    val name: Name,
    private val sex: Sex,
    val birthDate: LocalDate?,
    val sportCategory: Category,
    private val preferredGroup: GroupType = GroupType(""),
    val team: Team
) {
    val number: AthleteNumber = TODO()
    private val group: Group = TODO()
}


