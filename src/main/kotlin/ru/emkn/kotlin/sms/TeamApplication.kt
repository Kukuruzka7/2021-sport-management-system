package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import java.io.File

val applicationFormat = listOf("secondName", "firstName", "sex", "yearOfBirth", "sportCategory", "desiredGroup")

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
        val name = Name(row[1], row[0])
        val sex = when (row[2]) {
            "М" -> Sex.MALE
            else -> Sex.FEMALE
        }
        val birthDate = LocalDate(row[3].toInt(), 1, 1)
        val sportCategory = Category(row[4])
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
        if (row.size < 5) {
            throw ApplicationHasWrongFormatOnLine(numberOfApplication, row.toString())
        }
        if (row[2] != "М" && row[2] != "Ж") {
            throw WrongSexInApplicationOnLine(numberOfApplication, row[2])
        }
        if (row[3].toIntOrNull() == null) {
            throw WrongYearInApplicationOnLine(numberOfApplication, row[3])
        }
    }
}