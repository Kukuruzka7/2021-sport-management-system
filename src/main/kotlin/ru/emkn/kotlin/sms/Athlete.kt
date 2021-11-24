package ru.emkn.kotlin.sms

import kotlinx.datetime.LocalDate

class AthleteNumber(val value: String) {

}

enum class Sex {
    MALE, FEMALE
}

class Name(val firstName: String, val lastName: String) {
    val fullName: String = "$firstName $lastName"
}

class Category(val categoryName: String)

private var LastUseNumber = 1

class Athlete(
    val name: Name,
    val sex: Sex,
    val birthDate: LocalDate?,
    val sportCategory: Category,
    private val preferredGroup: Race = Race(""),
    val team: Team
) {
    val number: AthleteNumber
    lateinit var  group: Group

    init{
        number = numerate()
    }


    private fun numerate(): AthleteNumber {
        LastUseNumber++
        return AthleteNumber(LastUseNumber.toString())
    }
}


