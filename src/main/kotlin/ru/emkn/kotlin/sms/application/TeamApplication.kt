package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.athlete.Category
import ru.emkn.kotlin.sms.athlete.Sex
import ru.emkn.kotlin.sms.*
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
        team = Team(teamName, emptyList())
        team.athletes = processingData(rows)
    }

    private fun processingData(rows: List<List<String>>): List<Athlete> {
        return rows.subList(1, rows.size).map { processingRow(it) }
    }

    private fun processingRow(row: List<String>): Athlete {
        val name = Name(firstName = row[Fields.FIRST_NAME.ordinal], lastName = row[Fields.LAST_NAME.ordinal])
        val sex = Sex.getSex(row[Fields.SEX.ordinal])

        val birthDate = LocalDate(row[Fields.BIRTH_DATE.ordinal].toInt(), 1, 1)
        val sportCategory = Category.getCategory(row[Fields.SPORT_CATEGORY.ordinal])
        return Athlete(
            name,
            sex,
            birthDate,
            sportCategory,
            _teamName = teamName,
            _groupName = GroupName("$sex${birthDate.year}")
        )
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
        if (Sex.getSex(row[Fields.SEX.ordinal]) == Sex.X) {
            throw WrongSexInApplicationOnLine(numberOfApplication, row[Fields.SEX.ordinal])
        }
        if (row[Fields.BIRTH_DATE.ordinal].toIntOrNull() == null) {
            throw WrongYearInApplicationOnLine(numberOfApplication, row[Fields.BIRTH_DATE.ordinal])
        }
    }

    companion object {
        val APPLICATION_FORMAT =
            listOf("secondName", "firstName", "sex", "yearOfBirth", "sportCategory", "desiredGroup")

        enum class Fields {
            LAST_NAME, FIRST_NAME, SEX, BIRTH_DATE, SPORT_CATEGORY
        }
    }
}