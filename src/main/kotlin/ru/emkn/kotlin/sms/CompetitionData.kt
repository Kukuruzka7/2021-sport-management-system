package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.util.*

class AthleteData

class CompetitionData(val athletesData: List<List<String>>) {
    fun save(fileName: String) {
        try {
            csvWriter().open(fileName) {
                writeRow(firstRaw)
                athletesData.forEach { writeRow(it) }
            }
        } catch (_: Exception) {
            throw FileCouldNotBeCreated(fileName)
        }
    }

    companion object {
        val firstRaw = listOf("Номер", "Имя", "Год рождения", "Разряд", "Предпочитаемая группа", "Группа", "Команда")

    }
}