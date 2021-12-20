package ru.emkn.kotlin.sms.model.application

import androidx.compose.runtime.MutableState
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import logger
import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.model.Team
import ru.emkn.kotlin.sms.model.athlete.Athlete
import ru.emkn.kotlin.sms.model.athlete.Category
import ru.emkn.kotlin.sms.model.athlete.Name
import ru.emkn.kotlin.sms.model.athlete.Sex
import ru.emkn.kotlin.sms.view.table_view.toListListStr
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
class TeamApplication(var teamName: String, val rows: List<List<String>>, private val indexOfApplication: Int) {

    val team: Team

    constructor(application: TeamApplication, contentRows: MutableList<MutableList<MutableState<String>>>) : this(
        application.teamName,
        contentRows.toListListStr(),
        application.indexOfApplication
    )

    constructor(file: File, indexOfApplication: Int) : this(
        getRows(file, indexOfApplication)[0][0],
        getRows(file, indexOfApplication).drop(2),
        indexOfApplication
    )

    init {
        logger.trace { "Создание экземпляра класса TeamApplication(file = ${teamName})" }
        checkFormatOfApplication(indexOfApplication, rows)
        team = Team(teamName, emptyList())
        team.athletes = processingData(rows, teamName)
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
            rows.forEach {
                checkRow(it, numberOfApplication)
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
        fun processingRow(row: List<String>, teamName: String): Athlete {
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
        fun processingData(rows: List<List<String>>, teamName: String): List<Athlete> {
            logger.trace { "Вызов processingData(rows)" }
            return rows.map { processingRow(it, teamName) }
        }
    }
}