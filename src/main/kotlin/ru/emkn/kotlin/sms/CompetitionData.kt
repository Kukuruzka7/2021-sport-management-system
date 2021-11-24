package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

class AthleteData

//Номер,Имя,Пол,ГодРождения,Разряд, ПредпочтительнаяГруппа
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
            Fields.NUMBER to "Номер",
            Fields.NAME to "Имя",
            Fields.BIRTH_DATE to "Год рождения",
            Fields.SPORT_CATEGORY to "Разряд",
            Fields.TEAM_NAME to "Команда",
            Fields.RACE to "Группа",
            Fields.PREFERRED_GROUP to "Предпочтительная группа"
        )
    }
}