package ru.emkn.kotlin.sms

import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.athlete.Category
import ru.emkn.kotlin.sms.athlete.Sex

class AthleteNumber(val value: String) {
    override fun toString() = value
}

class Name(val firstName: String, val lastName: String) {
    //Нужно попарсить, требуется гении regex-а (aka Kukuruzka_7)
    constructor (_name: String) : this(TODO(), TODO())

    val fullName: String = "$firstName $lastName"

    override fun toString(): String = fullName
}

class Athlete(
    val name: Name,
    val sex: Sex,
    val birthDate: LocalDate,
    val sportCategory: Category,
    private val preferredGroup: GroupName = GroupName(""),
    val teamName: TeamName,
    val groupName: GroupName,
    val number: AthleteNumber
) {

    constructor(
        _name: Name,
        _sex: Sex,
        _birthDate: LocalDate,
        _sportCategory: Category,
        _preferredGroup: GroupName = GroupName("TODO()"),
        _teamName: TeamName,
        _groupName: GroupName
    ) : this(_name, _sex, _birthDate, _sportCategory, _preferredGroup, _teamName, _groupName, numerate())

    lateinit var race: Race

    fun extractFieldToString(field: CompetitionData.Companion.Fields): String = when (field) {
        CompetitionData.Companion.Fields.NUMBER -> number.toString()
        CompetitionData.Companion.Fields.NAME -> name.toString()
        CompetitionData.Companion.Fields.SEX -> sex.toString()
        CompetitionData.Companion.Fields.BIRTH_DATE -> birthDate.toString()
        CompetitionData.Companion.Fields.CATEGORY -> sportCategory.toString()
        CompetitionData.Companion.Fields.TEAM_NAME -> teamName.toString()
        CompetitionData.Companion.Fields.RACE -> race.toString()
        CompetitionData.Companion.Fields.PREFERRED_GROUP -> preferredGroup.toString()
    }

    companion object {
        private var lastUsedNumber = 1
        private fun numerate() = AthleteNumber(lastUsedNumber++.toString())
    }
}


