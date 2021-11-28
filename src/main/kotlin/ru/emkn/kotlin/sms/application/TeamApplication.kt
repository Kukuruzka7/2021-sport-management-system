package ru.emkn.kotlin.sms.application

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.athlete.Athlete
import ru.emkn.kotlin.sms.athlete.Category
import ru.emkn.kotlin.sms.athlete.Name
import ru.emkn.kotlin.sms.athlete.Sex
import java.io.File
import logger
import ru.emkn.kotlin.sms.*

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
        logger.trace { "Создание экземпляра класса TeamApplication(file = ${file.name})" }
        checkFormatOfApplication(numberOfApplication, rows)
        teamName = TeamName(rows[0][0])
        team = Team(teamName, emptyList())
        team.athletes = processingData(rows.subList(2, rows.size), teamName)
    }

    companion object {
        val APPLICATION_FORMAT =
            listOf("secondName", "firstName", "sex", "yearOfBirth", "sportCategory", "desiredGroup")

        enum class Fields {
            LAST_NAME, FIRST_NAME, SEX, BIRTH_DATE, SPORT_CATEGORY
        }

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

        fun processingData(rows: List<List<String>>, teamName: TeamName): List<Athlete> {
            logger.trace { "Вызов processingData(rows)" }
            return rows.map { processingRow(it, teamName) }
        }
    }
}