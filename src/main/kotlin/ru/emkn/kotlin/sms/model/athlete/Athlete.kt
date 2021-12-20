package ru.emkn.kotlin.sms.model.athlete

import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.GroupName
import ru.emkn.kotlin.sms.Race
import ru.emkn.kotlin.sms.model.CompetitionSerialization
import ru.emkn.kotlin.sms.model.SportType
import java.time.LocalTime

class AthleteNumber(val value: kotlin.String) {
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

class Name(val firstName: kotlin.String, val lastName: kotlin.String) {

    constructor (_name: kotlin.String) : this(getFirstName(_name), getLastName(_name))

    val fullName: kotlin.String = "$firstName $lastName"

    override fun toString(): kotlin.String = fullName

    companion object {
        private fun getFirstName(name: kotlin.String): kotlin.String {
            return name.split(" ")[0]
        }

        private fun getLastName(name: kotlin.String): kotlin.String {
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
    val teamName: String,
    val groupName: GroupName,
    val number: AthleteNumber
) {

    constructor(
        _name: Name,
        _sex: Sex,
        _birthDate: LocalDate,
        _sportCategory: Category,
        _preferredGroup: GroupName = GroupName("TODO()"),
        _teamName: String,
        _groupName: GroupName
    ) : this(_name, _sex, _birthDate, _sportCategory, _preferredGroup, _teamName, _groupName, numerate())

    val race = Race(groupName, SportType.ORIENTEERING) //TODO()
    lateinit var startTime: LocalTime

    override fun toString(): kotlin.String = "[$name, $number, $groupName]"

    fun extractFieldToString(field: CompetitionSerialization.Companion.Fields): kotlin.String = when (field) {
        CompetitionSerialization.Companion.Fields.NUMBER -> number.toString()
        CompetitionSerialization.Companion.Fields.NAME -> name.toString()
        CompetitionSerialization.Companion.Fields.SEX -> sex.toString()
        CompetitionSerialization.Companion.Fields.BIRTH_DATE -> birthDate.toString()
        CompetitionSerialization.Companion.Fields.CATEGORY -> sportCategory.toString()
        CompetitionSerialization.Companion.Fields.TEAM_NAME -> teamName.toString()
        CompetitionSerialization.Companion.Fields.RACE -> race.toString()
        CompetitionSerialization.Companion.Fields.PREFERRED_GROUP -> preferredGroup.toString()
        CompetitionSerialization.Companion.Fields.START_TIME -> startTime.toString()
    }

    companion object {
        var lastUsedNumber = 1
        private fun numerate() = AthleteNumber(lastUsedNumber++.toString())
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Athlete) {
            return false
        }
        return (this.name.fullName == other.name.fullName) && (this.groupName.value == other.groupName.value)
    }
}

fun Athlete.toStringList() =
    CompetitionSerialization.Companion.Fields.values().map { this.extractFieldToString(it) }

