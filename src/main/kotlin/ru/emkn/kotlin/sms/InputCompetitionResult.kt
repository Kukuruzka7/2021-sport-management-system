package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import java.io.File

interface InputCompetitionResult {
    fun toTable(): Table
}

class InputCompetitionResultByAthletes(fileNames: List<String>) :
    InputCompetitionResult {
    val results = fileNames.map { InputAthleteResults(it) }
    override fun toTable() = Table(results.associateBy({ it.number }, { it.resultsOnCheckPoints }))

}

class InputAthleteResults(val fileName: String) {
    val resultsOnCheckPoints: Map<CheckPoint, CheckPointRes>
    val number: AthleteNumber

    init {
        val rows = getRows()
        val checkPointsResults = rows.drop(0)
        number = getAthleteNumber(checkPointsResults)
        resultsOnCheckPoints = checkPointsResults.map { parseRow(it) }.associateBy({ it.checkPoint }, { it })
    }

    companion object {
        val ATHLETE_RESULT_FORMAT = listOf("checkpoint", "date")
        val CHECKPOINT_NAME_INDEX_IN_ROW = 0
        val ATHLETE_TIME_ON_CHECKPOINT_INDEX_IN_ROW = 1
    }

    private fun getAthleteNumber(rows: List<List<String>>): AthleteNumber {
        if (rows.isEmpty()) {
            throw ResultMissesAthleteNumber("Пустое файл")
        }
        if (rows[0].isEmpty()) {
            throw ResultMissesAthleteNumber(fileName)
        }
        return AthleteNumber(rows[0][0])
    }

    private fun getRows(): List<List<String>> = try {
        csvReader().readAll(File(fileName).toString())
    } catch (_: Exception) {
        throw ResultCanNotBeRead(fileName)
    }


    private fun parseRow(row: List<String>): CheckPointRes {
        if (row.size < 2) {
            throw ResultByAthleteInvalidRow(fileName, row)
        }
        val time = try {
            LocalDate.parse(row[ATHLETE_TIME_ON_CHECKPOINT_INDEX_IN_ROW])
        } catch (_: java.time.format.DateTimeParseException) {
            throw InvalidDateFormat(fileName, row[ATHLETE_TIME_ON_CHECKPOINT_INDEX_IN_ROW])
        }
        return CheckPointRes(CheckPoint(row[CHECKPOINT_NAME_INDEX_IN_ROW]), time)
    }

}

class InputCompetitionResultByCheckPoints(val result: List<InputCheckpointResults>) : InputCompetitionResult {

    override fun toTable(): Table {
        TODO("Not yet implemented")
    }
}

class InputCheckpointResults(fileName: List<String>) {

}
