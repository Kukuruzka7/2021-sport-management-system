package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import java.io.File

interface InputCompetitionResult {
    fun toTable(): Table
}

interface InputResult {
    val fileName: String
    fun getRows(): List<List<String>> = try {
        csvReader().readAll(File(fileName).toString())
    } catch (_: Exception) {
        throw ResultCanNotBeRead(fileName)
    }
}

class InputCompetitionResultByAthletes(fileNames: List<String>) : InputCompetitionResult {
    val resultsOnPoints = fileNames.map { InputAthleteResults(it) }
    override fun toTable() = Table(resultsOnPoints.associateBy({ it.number }, { it.resultsOnCheckPoints }))

}

class InputAthleteResults(override val fileName: String) : InputResult {
    val resultsOnCheckPoints: Map<CheckPoint, CheckPointRes>
    val number: AthleteNumber

    init {
        val rows = getRows()
        val checkPointsResults = rows.drop(0)
        number = getAthleteNumber(checkPointsResults)
        resultsOnCheckPoints = checkPointsResults.map { parseRow(it, number) }.associateBy({ it.checkPoint }, { it })
    }

    companion object {
        val ATHLETE_RESULT_FORMAT = listOf("checkpoint", "date")
        val NUMBER_OF_ARGUMENTS_IN_ROW = ATHLETE_RESULT_FORMAT.size
        val CHECKPOINT_NAME_INDEX_IN_ROW = 0
        val TIME_ON_CHECKPOINT_INDEX_IN_ROW = 1
    }

    private fun getAthleteNumber(rows: List<List<String>>): AthleteNumber {
        if (rows.isEmpty() || rows[0].isEmpty()) {
            throw ResultMissesAthleteNumber(fileName)
        }
        return AthleteNumber(rows[0][0])
    }


    private fun parseRow(row: List<String>, athleteNumber: AthleteNumber): CheckPointRes {
        if (row.size < 2) {
            throw ResultByAthleteInvalidRow(fileName, row)
        }
        val time = try {
            LocalDate.parse(row[TIME_ON_CHECKPOINT_INDEX_IN_ROW])
        } catch (_: java.time.format.DateTimeParseException) {
            throw InvalidDateFormat(fileName, row[TIME_ON_CHECKPOINT_INDEX_IN_ROW])
        }
        return CheckPointRes(CheckPoint(row[CHECKPOINT_NAME_INDEX_IN_ROW]), athleteNumber, time)
    }

}

class InputCompetitionResultByCheckPoints(fileNames: List<String>) : InputCompetitionResult {
    val resultsOnPoints = fileNames.map { InputCheckpointResults(it) }
    override fun toTable(): Table {
        val listOfCheckPointRes = resultsOnPoints.flatMap { it.resultsOfAthletes.values }
        return Table(listToMapOfMaps(listOfCheckPointRes))
    }

    private fun listToMapOfMaps(list: List<CheckPointRes>): Map<AthleteNumber, Map<CheckPoint, CheckPointRes>> =
        list.groupBy { it.athleteNumber }.mapValues { listToMap(it.value) }

    private fun listToMap(list: List<CheckPointRes>): Map<CheckPoint, CheckPointRes> =
        list.groupBy { it.checkPoint }.mapValues { it.value[0] }
}


class InputCheckpointResults(override val fileName: String) : InputResult {
    val resultsOfAthletes: Map<AthleteNumber, CheckPointRes>
    val checkPoint: CheckPoint

    init {
        val rows = getRows()
        val athletesResults = rows.drop(0)
        checkPoint = getCheckPoint(athletesResults)
        resultsOfAthletes = athletesResults.map { parseRow(it, checkPoint) }.associateBy({ it.athleteNumber }, { it })
    }

    companion object {
        val CHECKPOINT_RESULT_FORMAT = listOf("athlete number", "date")
        val NUMBER_OF_ARGUMENTS_IN_ROW = CHECKPOINT_RESULT_FORMAT.size
        val ATHLETE_NUM_INDEX_IN_ROW = 0
        val ATHLETE_TIME_INDEX_IN_ROW = 1
    }

    private fun getCheckPoint(rows: List<List<String>>): CheckPoint {
        if (rows.isEmpty() || rows[0].isEmpty()) {
            throw ResultMissesCheckPointName(fileName)
        }
        return CheckPoint(rows[0][0])
    }

    private fun parseRow(row: List<String>, checkPoint: CheckPoint): CheckPointRes {
        if (row.size < NUMBER_OF_ARGUMENTS_IN_ROW) {
            throw ResultByCheckpointInvalidRow(fileName, row)
        }
        val time = try {
            LocalDate.parse(row[ATHLETE_TIME_INDEX_IN_ROW])
        } catch (_: java.time.format.DateTimeParseException) {
            throw InvalidDateFormat(fileName, row[ATHLETE_TIME_INDEX_IN_ROW])
        }
        return CheckPointRes(checkPoint, AthleteNumber(row[ATHLETE_NUM_INDEX_IN_ROW]), time)
    }
}
