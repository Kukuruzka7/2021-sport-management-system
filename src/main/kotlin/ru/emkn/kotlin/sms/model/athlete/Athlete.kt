package ru.emkn.kotlin.sms.model.athlete

import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.GroupName
import ru.emkn.kotlin.sms.Race
import ru.emkn.kotlin.sms.model.CompetitionData
import ru.emkn.kotlin.sms.model.SportType
import ru.emkn.kotlin.sms.model.TeamName
import java.time.LocalTime

class AthleteNumber(val value: String) {
    override fun toString() = value
    override fun equals(other: Any?): Boolean {
        if (other !is AthleteNumber) {
            return false
        }
        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

class Name(val firstName: String, val lastName: String) {

    constructor (_name: String) : this(getFirstName(_name), getLastName(_name))

    val fullName: String = "$firstName $lastName"

    override fun toString(): String = fullName

    companion object {
        private fun getFirstName(name: String): String {
            return name.split(" ")[0]
        }

        private fun getLastName(name: String): String {
            if (name.split(" ").size == 1) {
                return ""
            }
            return name.substringAfter(" ")
        }
    }
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

    val race = Race(groupName, SportType.RUNNING)
    lateinit var startTime: LocalTime

    override fun toString(): String = "[$name, $number, $groupName]"

    fun extractFieldToString(field: CompetitionData.Companion.Fields): String = when (field) {
        CompetitionData.Companion.Fields.NUMBER -> number.toString()
        CompetitionData.Companion.Fields.NAME -> name.toString()
        CompetitionData.Companion.Fields.SEX -> sex.toString()
        CompetitionData.Companion.Fields.BIRTH_DATE -> birthDate.toString()
        CompetitionData.Companion.Fields.CATEGORY -> sportCategory.toString()
        CompetitionData.Companion.Fields.TEAM_NAME -> teamName.toString()
        CompetitionData.Companion.Fields.RACE -> race.toString()
        CompetitionData.Companion.Fields.PREFERRED_GROUP -> preferredGroup.toString()
        CompetitionData.Companion.Fields.START_TIME -> startTime.toString()
    }

    companion object {
        var lastUsedNumber = 1
        private fun numerate() = AthleteNumber(lastUsedNumber++.toString())
    }

    override fun equals(other: Any?): Boolean {
        if(other !is Athlete) {
            return false
        }
        return (this.name.fullName == other.name.fullName) && (this.groupName.value == other.groupName.value)
    }
}

