package ru.emkn.kotlin.sms.finishprotocol

import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import ru.emkn.kotlin.sms.Group
import java.io.File

class GroupProtocol(private val group: Group, val protocol: List<AthleteProtocol>) {
    fun toCSV(dirName: String) {
        val fileName = """$dirName${group.race.groupName}.csv"""
        File(fileName).createNewFile()
        CsvWriter().open(fileName) {
            writeRow(group.race.groupName)
            protocol.forEach { writeAthleteProtocol(it) }
        }
    }
}