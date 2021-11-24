package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

class AthleteData

class CompetitionData(val athletesData: List<List<String>>) {
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


    fun toAthletesList(): List<Athlete> {
        TODO()
    }

    companion object {
        enum class Fields {
            NUMBER, NAME, BIRTH_DATE, SPORT_CATEGORY, TEAM_NAME, RACE, PREFERRED_GROUP,
        }

        val fieldToRussian = mapOf(
            Fields.NUMBER to "номер",
            Fields.NAME to "имя",
            Fields.BIRTH_DATE to "год рождения",
            Fields.SPORT_CATEGORY to "разряд",
            Fields.TEAM_NAME to "команда",
            Fields.RACE to "группа",
            Fields.PREFERRED_GROUP to "предпочтительная группа"
        )
    }
}