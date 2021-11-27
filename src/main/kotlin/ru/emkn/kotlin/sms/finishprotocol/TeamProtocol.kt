package ru.emkn.kotlin.sms.finishprotocol

import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import ru.emkn.kotlin.sms.Team
import java.io.File

class TeamProtocol(private val team: Team, private val protocol: List<AthleteProtocol>) {
    fun toCSV(dirName: String) {
        val fileName = """$dirName${team.teamName}.csv"""
        File(fileName).createNewFile()
        CsvWriter().open(fileName) {
            writeRow(team.teamName)
            writeInfoRow()
            protocol.sortedBy { it.athlete.groupName.groupName }.forEach { writeAthleteProtocol(it) }
        }
    }
}