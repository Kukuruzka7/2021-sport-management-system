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
                writeRow(Fields.values().joinToString { it.toRussian() })
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
            GroupName(it[Fields.PREFERRED_GROUP.ordinal]),
            TeamName(it[Fields.TEAM_NAME.ordinal]),
            GroupName(it[Fields.RACE.ordinal]),
            AthleteNumber(it[Fields.NUMBER.ordinal]),
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

            fun toRussian() = fieldToRussian[this]!!

            companion object {
                private val fieldToRussian = mapOf(
                    NUMBER to "номер",
                    NAME to "имя",
                    SEX to "пол",
                    BIRTH_DATE to "год рождения",
                    CATEGORY to "разряд",
                    TEAM_NAME to "команда",
                    RACE to "группа",
                    PREFERRED_GROUP to "предпочтительная группа"
                )
            }

        }
        val inputFormat = Fields.values().joinToString {
            it.toRussian()
        }
    }
}