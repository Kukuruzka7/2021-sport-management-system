package ru.emkn.kotlin.sms.finishprotocol

import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import logger
import ru.emkn.kotlin.sms.FileCouldNotBeCreated
import ru.emkn.kotlin.sms.Group
import java.io.File

class GroupProtocol(val group: Group, val protocol: List<AthleteProtocol>) {
    fun toCSV(dirName: String) {
        //Пытаемся создать файл filename
        val fileName = """$dirName${group.race.groupName}.csv"""
        try {
            logger.trace { "Пытаемся создать файл с названием $fileName" }
            File(fileName).createNewFile()
        } catch (_: Exception) {
            logger.error {FileCouldNotBeCreated(fileName)}
            throw FileCouldNotBeCreated(fileName)
        }
        //Заполняем файл
        CsvWriter().open(fileName) {
            //Пишем названием команды и первую строчку ("№ п/п", "Номер",  "Фамилия" и т.д.)
            writeRow(listOf(group.race.groupName,"","","","","","","","",""))
            writeInfoRow()
            protocol.forEach { writeAthleteProtocol(it) }
        }
    }
}