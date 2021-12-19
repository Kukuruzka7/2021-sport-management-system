package ru.emkn.kotlin.sms.model.application

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import logger
import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.model.Team
import ru.emkn.kotlin.sms.model.TeamName
import ru.emkn.kotlin.sms.model.athlete.Athlete
import ru.emkn.kotlin.sms.model.athlete.Category
import ru.emkn.kotlin.sms.model.athlete.Name
import ru.emkn.kotlin.sms.model.athlete.Sex
import java.io.File

fun getRows(file: File, indexOfApplication: Int): List<List<String>> =
    try {
        logger.trace { "Считывание данных об атлетах из ${file.name}." }
        csvReader().readAll(file)
    } catch (e: Exception) {
        logger.error { ApplicationCanNotBeRead(indexOfApplication) }
        throw ApplicationCanNotBeRead(indexOfApplication)
    }

//по данным из заявки получение данных об атлетах
class TeamApplication(fileName: String, val rows: List<List<String>>, indexOfApplication: Int) {

    val teamName: TeamName
    val team: Team

    constructor(file: File, indexOfApplication: Int) : this(
        file.name,
        getRows(file, indexOfApplication),
        indexOfApplication
    )

    init {
        logger.trace { "Создание экземпляра класса TeamApplication(file = ${fileName})" }
        checkFormatOfApplication(indexOfApplication, rows)
        teamName = rows[0][0]
        team = Team(teamName, emptyList())
        team.athletes = processingData(rows.subList(2, rows.size), teamName)
    }

    companion object {
        val APPLICATION_FORMAT =
            listOf("secondName", "firstName", "sex", "yearOfBirth", "sportCategory", "desiredGroup")

        enum class Fields {
            LAST_NAME, FIRST_NAME, SEX, BIRTH_DATE, SPORT_CATEGORY
        }

        // private
        fun checkFormatOfApplication(numberOfApplication: Int, rows: List<List<String>>) {
            logger.trace { "Вызов checkFormatOfApplication(${numberOfApplication})" }
            if (rows.isEmpty()) {
                logger.error { "В заявке $numberOfApplication нет данных об атлетах." }
                throw ApplicationHasWrongFormat(numberOfApplication)
            }
            for (i in 2..rows.lastIndex) {
                checkRow(rows[i], numberOfApplication)
            }
        }
        //private
        fun checkRow(row: List<String>, numberOfApplication: Int) {
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
            if (Category.getCategory(row[Fields.SPORT_CATEGORY.ordinal]) == Category.X) {
                logger.error { "В заявке $numberOfApplication в $row неправильная дата рождения." }
                throw WrongCategoryInApplicationOnLine(numberOfApplication, row[Fields.BIRTH_DATE.ordinal])
            }
        }
        //private
        fun processingRow(row: List<String>, teamName: TeamName): Athlete {
            logger.trace { "Вызов processingRow(row.size = ${row.size})" }
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
        //private
        fun processingData(rows: List<List<String>>, teamName: TeamName): List<Athlete> {
            logger.trace { "Вызов processingData(rows)" }
            return rows.map { processingRow(it, teamName) }
        }
    }
}