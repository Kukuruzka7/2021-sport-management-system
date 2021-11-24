package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import java.io.File

class TeamApplication(file: File, val numberOfApplication: Int) {

    private val rows: List<List<String>> = try {
        csvReader().readAll(file)
    } catch (e: Exception) {
        throw ApplicationCanNotBeRead(numberOfApplication)
    }
    val teamName: TeamName
    val team: Team

    init {
        checkFormatOfApplication(numberOfApplication, rows)
        teamName = TeamName(rows[0][0])
        team = Team(teamName)
        team.athleteList = processingData(rows)
    }


    private fun processingData(rows: List<List<String>>): List<Athlete> {
        return rows.subList(1, rows.size).map { processingRow(it) }
    }

    private fun processingRow(row: List<String>): Athlete {
        val name = Name(firstName = row[Fields.FIRST_NAME.ordinal], lastName = row[Fields.LAST_NAME.ordinal])
        val sex = getSex(row[Fields.SEX.ordinal])

        val birthDate = LocalDate(row[Fields.BIRTH_DATE.ordinal].toInt(), 1, 1)
        val sportCategory = Category(row[Fields.SPORT_CATEGORY.ordinal])
        return Athlete(name, sex, birthDate, sportCategory, team = team)
    }

    private fun checkFormatOfApplication(numberOfApplication: Int, rows: List<List<String>>) {
        if (rows.isEmpty()) {
            throw ApplicationHasWrongFormat(numberOfApplication)
        }
        for (i in 1..rows.lastIndex) {
            checkRow(rows[i])
        }
    }

    private fun checkRow(row: List<String>) {
        if (row.size < Fields.values().size) {
            throw ApplicationHasWrongFormatOnLine(numberOfApplication, row.toString())
        }
        if (getSex(row[Fields.SEX.ordinal]) == Sex.X) {
            throw WrongSexInApplicationOnLine(numberOfApplication, row[Fields.SEX.ordinal])
        }
        if (row[Fields.BIRTH_DATE.ordinal].toIntOrNull() == null) {
            throw WrongYearInApplicationOnLine(numberOfApplication, row[Fields.BIRTH_DATE.ordinal])
        }
    }

    companion object {
        val maleDescriptions = setOf("m", "M", "male", "man", "м", "М", "муж", "мужской", "мужчина")
        val femaleDescriptions = setOf("w", "W", "female", "woman", "ж", "Ж", "жен", "женский", "женщина")
        val APPLICATION_FORMAT =
            listOf("secondName", "firstName", "sex", "yearOfBirth", "sportCategory", "desiredGroup")

        //Карта, которая по описанию пола возвращает пол (см. выше)
        val sexMap = buildSexMap(maleDescriptions, femaleDescriptions)

        private fun buildSexMap(male: Set<String>, female: Set<String>): Map<String, Sex> {
            val result = maleDescriptions.associateWith { Sex.MALE }.toMutableMap()
            result.putAll(femaleDescriptions.associateWith { Sex.FEMALE })
            return result
        }

        //Возвращает пол, если пол указан корректно, и Sex.X иначе
        fun getSex(value: String): Sex = sexMap[value] ?: Sex.X

        enum class Fields {
            LAST_NAME, FIRST_NAME, SEX, BIRTH_DATE, SPORT_CATEGORY
        }
    }
}