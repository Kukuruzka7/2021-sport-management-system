package ru.emkn.kotlin.sms

import java.io.File
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

class GroupName(val groupName: String) {
    override fun toString() = groupName
}

class Race(val groupName: GroupName, sportType: SportType) {
    val checkPoints = getCheckPoints(groupName.groupName, sportType)
    override fun toString() = groupName.toString()

    private fun getCheckPoints(groupName: String, sportType: SportType): List<CheckPoint> {
        val course = classesBySportType[sportType]?.find { it[0] == groupName }?.get(1)
            ?: throw WeHaveAProblem("Здесь не должно быть null")
        val courseList = coursesBySportType[sportType]?.find { it[0] == course }
            ?: throw WeHaveAProblem("Здесь не должно быть null")
        return courseList.subList(1, courseList.lastIndex).map { CheckPoint(it) }
    }

    companion object {
        const val dir = "src/main/resources/races/"
        val classesBySportType =
            SportType.values().map { it to csvReader().readAll(File(dir + it.toString() + "/classes.csv")) }.toMap()
        val coursesBySportType =
            SportType.values().map { it to csvReader().readAll(File(dir + it.toString() + "/courses.csv")) }.toMap()
    }
}

class Group(val race: Race, var athletes: List<Athlete>)

