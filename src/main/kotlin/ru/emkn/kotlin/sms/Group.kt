package ru.emkn.kotlin.sms

import java.io.File
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.result_data.Checkpoint

class GroupName(val groupName: String) {
    override fun toString() = groupName
}

class Race(val groupName: GroupName, sportType: SportType) {
    val checkPoints = getCheckPoints(groupName.groupName, sportType)
    override fun toString() = groupName.toString()

    private fun getCheckPoints(groupName: String, sportType: SportType): List<Checkpoint> {
        val course = classesBySportType[sportType]?.find { it[0] == groupName }?.get(1)
            ?: throw WeHaveAProblem("Здесь не должно быть null")
        val courseList = coursesBySportType[sportType]?.find { it[0] == course }
            ?: throw WeHaveAProblem("Здесь не должно быть null")
        return courseList.subList(1, courseList.lastIndex).map { Checkpoint(it) }
    }

    companion object {
        const val dir = "src/main/resources/races/"
        val classesBySportType =
            SportType.values().associateWith { csvReader().readAll(File("$dir$it/classes.csv")) }
        val coursesBySportType =
            SportType.values().associateWith { csvReader().readAll(File("$dir$it/courses.csv")) }
    }
}

class Group(val race: Race, var athletes: List<Athlete>)

