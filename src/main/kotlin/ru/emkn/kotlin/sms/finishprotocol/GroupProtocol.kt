package ru.emkn.kotlin.sms.finishprotocol

import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import ru.emkn.kotlin.sms.FileCouldNotBeCreated
import ru.emkn.kotlin.sms.Group
import java.io.File

class GroupProtocol(val group: Group, val protocol: List<AthleteProtocol>) {
    fun toCSV(dirName: String) {
        //пытаемся создать файл filename
        val fileName = """$dirName${group.race.groupName}.csv"""
        try {
            File(fileName).createNewFile()
        } catch (_: Exception) {
            throw FileCouldNotBeCreated(fileName)
        }
        //заполняем файл
        CsvWriter().open(fileName) {
            //пишем названием команды и первую строчку ("№ п/п", "Номер",  "Фамилия" и т.д.)
            writeRow(group.race.groupName)
            writeInfoRow()
            protocol.forEach { writeAthleteProtocol(it) }
        }
    }
}