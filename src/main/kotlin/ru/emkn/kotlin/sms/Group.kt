package ru.emkn.kotlin.sms

import java.io.File
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import logger
import ru.emkn.kotlin.sms.athlete.Athlete
import ru.emkn.kotlin.sms.result_data.Checkpoint

class GroupName(val groupName: String) {
    override fun toString() = groupName
}

class Race(val groupName: GroupName) {
    val checkPoints = getCheckPoints(groupName.groupName)
    override fun toString() = groupName.toString()

    private fun getCheckPoints(groupName: String): List<Checkpoint> {
        logger.trace { "Вызов getCheckPoints()" }
        val course = classesBySportType[sport]?.find { it[0] == groupName }?.get(1)
        if (course == null) {
            logger.error { WeHaveAProblem("Здесь не должно быть null") };
            throw WeHaveAProblem("Здесь не должно быть null")
        }
        val courseList = coursesBySportType[sport]?.find { it[0] == course }
        if (courseList == null) {
            logger.error { WeHaveAProblem("Здесь не должно быть null") };
            throw WeHaveAProblem("Здесь не должно быть null")
        }
        return courseList.subList(1, courseList.lastIndex).filter { it != "" }.map { Checkpoint(it) }
    }

    companion object {
        const val dir = "src/main/resources/races/"
        val classesBySportType =
            SportType.values().slice(0 until SportType.values().size - 1)
                .associateWith { csvReader().readAll(File("$dir$it/classes.csv")) }
        val coursesBySportType =
            SportType.values().slice(0 until SportType.values().size - 1)
                .associateWith { csvReader().readAll(File("$dir$it/courses.csv")) }
    }
}

class Group(val race: Race, var athletes: List<Athlete>)

