package ru.emkn.kotlin.sms

import java.io.File
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import logger
import ru.emkn.kotlin.sms.athlete.Athlete
import ru.emkn.kotlin.sms.result_data.Checkpoint

class GroupName(val value: String) {
    override fun toString() = value
}

class Race(val groupName: GroupName, val sportType: SportType) {
    val checkPoints = getCheckPoints(groupName.value, sportType)

    override fun toString() = groupName.toString()

    override operator fun equals(other: Any?): Boolean {
        if (other !is Race) {
            return false
        }
        return groupName.value == other.groupName.value
    }


    companion object {
        const val dir = "src/main/resources/races/"
        val classesBySportType =
            SportType.values().map { csvReader().readAll(File("$dir${it}/classes.csv")) }
        val coursesBySportType =
            SportType.values().map { csvReader().readAll(File("$dir${it}/courses.csv")) }

        fun getCheckPoints(groupName: String, sportType: SportType): List<Checkpoint> {
            logger.trace { "Вызов getCheckPoints()" }
            val courseRow = classesBySportType[sportType.ordinal].find { it[0] == groupName }
            require(courseRow != null)
            val course = courseRow[1]
            val courseList = coursesBySportType[sportType.ordinal].find { it[0] == course }
            require(courseList != null)
            return courseList.subList(1, courseList.lastIndex).filter { it != "" }.map { Checkpoint(it) }
        }
    }
}

class Group(val race: Race, var athletes: List<Athlete>)

