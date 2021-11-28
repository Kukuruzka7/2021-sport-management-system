package ru.emkn.kotlin.sms.finishprotocol

import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import logger
import ru.emkn.kotlin.sms.FileCouldNotBeCreated
import ru.emkn.kotlin.sms.Team
import java.io.File

class TeamProtocol(private val team: Team, private val protocol: List<AthleteProtocol>) {
    fun toCSV(dirName: String) {
        //Пытаемся создать файл с названием fileName
        val fileName = "$dirName${team.teamName}.csv"
        try {
            logger.trace { "Пытаемся создать файл с названием $fileName" }
            File(fileName).createNewFile()
        } catch (_: Exception) {
            logger.error { FileCouldNotBeCreated(fileName) }
            throw FileCouldNotBeCreated(fileName)
        }
        //Заполняем файл с командным протоколом
        CsvWriter().open(fileName) {
            //Пишем названием команды и первую строчку ("№ п/п", "Номер",  "Фамилия" и т.д.)
            writeRow(listOf(team.teamName,"","","","","","","","",""))
            writeInfoRow()
            //Пишем собственно результаты атлетов
            protocol.sortedBy { it.athlete.groupName.value }.forEach { writeAthleteProtocol(it) }
        }
    }
}