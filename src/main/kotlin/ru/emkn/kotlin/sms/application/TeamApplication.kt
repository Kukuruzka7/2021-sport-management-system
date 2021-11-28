package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.athlete.Athlete
import ru.emkn.kotlin.sms.athlete.Category
import ru.emkn.kotlin.sms.athlete.Name
import ru.emkn.kotlin.sms.athlete.Sex
import java.io.File
import logger

//по данным из заявки получение данных об атлетах
class TeamApplication(file: File, val numberOfApplication: Int) {
    private val rows: List<List<String>> = try {
        logger.trace { "Считывание данных об атлетах из ${file.name}." }
        csvReader().readAll(file)
    } catch (e: Exception) {
        println(e.message)
        logger.error { ApplicationCanNotBeRead(numberOfApplication) }
        throw ApplicationCanNotBeRead(numberOfApplication)
    }
    val teamName: TeamName
    val team: Team

    init {
        logger.trace { "Инит-блок класса TeamApplication(file = ${file.name})" }
        checkFormatOfApplication(numberOfApplication, rows)
        teamName = TeamName(rows[0][0])
        team = Team(teamName, emptyList())
        team.athletes = processingData(rows.subList(2, rows.size))
        logger.trace { "Инит-блок класса TeamApplication завершен" }
    }

    //обработка строк
    private fun processingData(rows: List<List<String>>): List<Athlete> {
        logger.trace { "Вызов processingData($rows)" }
        return rows.map { processingRow(it) }
    }

    //обработка отдельной строки
    private fun processingRow(row: List<String>): Athlete {
        logger.trace { "Вызов processingRow(${row})" }
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
            _groupName = GroupName("$sex${birthDate.year}"),
        )
    }

    private fun checkFormatOfApplication(numberOfApplication: Int, rows: List<List<String>>) {
        logger.trace { "Вызов checkFormatOfApplication(${numberOfApplication})" }
        if (rows.isEmpty()) {
            logger.error { "В заявке $numberOfApplication нет данных об атлетах." }
            throw ApplicationHasWrongFormat(numberOfApplication)
        }
        for (i in 2..rows.lastIndex) {
            checkRow(rows[i])
        }
    }

    private fun checkRow(row: List<String>) {
        logger.trace { "Вызов checkRow(${row})" }
        if (row.size < Fields.values().size) {
            logger.error { "В заявке $numberOfApplication в $row нехватает данных." }
            throw ApplicationHasWrongFormatOnLine(numberOfApplication, row.toString())
        }
        if (Sex.getSex(row[Fields.SEX.ordinal]) == Sex.X) {
            logger.error { "В заявке $numberOfApplication в $row неверный пол." }
            throw WrongSexInApplicationOnLine(numberOfApplication, row[Fields.SEX.ordinal])
        }
        if (row[Fields.BIRTH_DATE.ordinal].toIntOrNull() == null) {
            logger.error { "В заявке $numberOfApplication в $row неправильная дата рождения." }
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