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

class Category(val categoryName: String)

private var LastUseNumber = 1

class Athlete(
    val name: Name,
    val sex: Sex,
    val birthDate: LocalDate,
    val sportCategory: Category,
    val number: AthleteNumber,
    val teamName: TeamName,
    var race: Race,
    private val preferredGroup: Race = Race("")
) {

    constructor(
        _name: Name,
        _sex: Sex,
        _birthDate: LocalDate,
        _sportCategory: Category,
        _teamName: TeamName,
        _race: Race,
        _preferredGroup: Race = Race("")
    ) : this(_name, _sex, _birthDate, _sportCategory, numerate(), _teamName, _race, _preferredGroup)


    companion object {
        private fun numerate() = AthleteNumber(LastUseNumber++.toString())
    }
}


