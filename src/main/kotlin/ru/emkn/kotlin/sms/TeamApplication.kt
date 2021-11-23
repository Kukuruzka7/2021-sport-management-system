package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File

class TeamApplication(file: File) {
    //добавить проверку на существование файлов
    private val rows: List<List<String>> = csvReader().readAll(file)
    val teamName: TeamName
    private val team: Team

    init {
        teamName = getTeamName(rows)
        team = Team(teamName, processingData(rows))
    }

    private fun getTeamName(rows: List<List<String>>): TeamName {
        //обработка не корректных данных
        return TeamName(rows[0][0])
    }

    private fun processingData(rows: List<List<String>>): List<Athlete> {
        //обработка случая размера 1
        return rows.subList(1, rows.size).map { processingRow(it) }
    }

    private fun processingRow(row: List<String>): Athlete {
        TODO()
    }
}