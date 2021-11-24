package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.athlete.Category
import ru.emkn.kotlin.sms.athlete.Sex


class CompetitionData(private val athletesData: List<List<String>>) {

    init {
        athletesData.forEach { checkRow(it) }
    }

    fun save(fileName: String) {
        try {
            csvWriter().open(fileName) {
                writeRow(Fields.values().joinToString { fieldToRussian[it]!! })
                athletesData.forEach { writeRow(it) }
            }
        } catch (_: Exception) {
            throw FileCouldNotBeCreated(fileName)
        }
    }


    fun toAthletesList(): List<Athlete> = athletesData.map {
        Athlete(
            Name(it[Fields.NAME.ordinal]),
            Sex.getSex(it[Fields.SEX.ordinal]),
            LocalDate.parse(it[Fields.BIRTH_DATE.ordinal]),
            Category.getCategory(it[Fields.CATEGORY.ordinal]),
            TeamName(it[Fields.TEAM_NAME.ordinal]),
            AthleteNumber(it[Fields.NUMBER.ordinal]),
            Race(it[Fields.RACE.ordinal])
        )
    }

    private fun checkRow(row: List<String>) {
        if (row.size < Fields.values().size) {
            throw CompetitionDataTooFewArgumentsInRow(row)
        }
        if (Sex.getSex(row[Fields.SEX.ordinal]) == Sex.X) {
            throw CompetitionDataInvalidSex(row[Fields.SEX.ordinal])
        }
        try {
            LocalDate.parse(row[Fields.BIRTH_DATE.ordinal])
        } catch (_: java.time.format.DateTimeParseException) {
            throw CompetitionDataInvalidDate(row[Fields.BIRTH_DATE.ordinal])
        }
        if (Category.getCategory(row[Fields.CATEGORY.ordinal]) == Category.X) {
            throw CompetitionDataInvalidSportCategory(row[Fields.CATEGORY.ordinal])
        }
    }


    companion object {
        enum class Fields {
            NUMBER, NAME, SEX, BIRTH_DATE, CATEGORY, TEAM_NAME, RACE, PREFERRED_GROUP;

        }

        val inputFormat = Fields.values().joinToString { fieldToRussian[it]!! }

        val fieldToRussian = mapOf(
            Fields.NUMBER to "номер",
            Fields.NAME to "имя",
            Fields.SEX to "пол",
            Fields.BIRTH_DATE to "год рождения",
            Fields.CATEGORY to "разряд",
            Fields.TEAM_NAME to "команда",
            Fields.RACE to "группа",
            Fields.PREFERRED_GROUP to "предпочтительная группа"
        )
    }
}