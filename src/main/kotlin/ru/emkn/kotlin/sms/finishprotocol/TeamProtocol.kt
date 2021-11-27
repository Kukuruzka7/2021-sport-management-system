package ru.emkn.kotlin.sms.finishprotocol

import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import ru.emkn.kotlin.sms.FileCouldNotBeCreated
import ru.emkn.kotlin.sms.Team
import java.io.File

class TeamProtocol(private val team: Team, private val protocol: List<AthleteProtocol>) {
    fun toCSV(dirName: String) {
        //пытаемся создать файл с названием fileName
        val fileName = "$dirName${team.teamName}.csv"
        try {
            File(fileName).createNewFile()
        } catch (_: Exception) {
            throw FileCouldNotBeCreated(fileName)
        }
        //заполняем файл
        CsvWriter().open(fileName) {
            //пишем названием команды и первую строчку ("№ п/п", "Номер",  "Фамилия" и т.д.)
            writeRow(team.teamName)
            writeInfoRow()
            //пишем собственно результаты атлетов
            protocol.sortedBy { it.athlete.groupName.groupName }.forEach { writeAthleteProtocol(it) }
        }
    }
}