package ru.emkn.kotlin.sms.input_result

import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.*

class InputAthleteResults(override val fileName: String) : InputResult {
    val resultsOnCheckPoints: Map<CheckPoint, CheckPointRes>
    val number: AthleteNumber

    init {
        val rows = getRows()
        val checkPointsResults = rows.drop(0)
        number = getAthleteNumber(checkPointsResults)
        resultsOnCheckPoints = checkPointsResults.map { parseRow(it, number) }.associateBy({ it.checkPoint }, { it })
    }

    private fun getAthleteNumber(rows: List<List<String>>): AthleteNumber {
        if (rows.isEmpty() || rows[0].isEmpty()) {
            throw ResultMissesAthleteNumber(fileName)
        }
        return AthleteNumber(rows[0][0])
    }


    private fun parseRow(row: List<String>, athleteNumber: AthleteNumber): CheckPointRes {
        if (row.size < Fields.values().size) {
            throw ResultByAthleteInvalidRow(fileName, row)
        }
        val time = try {
            LocalDate.parse(row[Fields.TIME_ON_CHECKPOINT.ordinal])
        } catch (_: java.time.format.DateTimeParseException) {
            throw InvalidDateFormat(fileName, row[Fields.TIME_ON_CHECKPOINT.ordinal])
        }
        return CheckPointRes(CheckPoint(row[Fields.CHECKPOINT_NAME.ordinal]), athleteNumber, TODO("Нужен тип LocalDateTime"))
    }


    companion object {
        val ATHLETE_RESULT_FORMAT =
            mapOf(Fields.CHECKPOINT_NAME to "checkpoint name", Fields.TIME_ON_CHECKPOINT to "time on checkpoint")

        enum class Fields {
            CHECKPOINT_NAME, TIME_ON_CHECKPOINT
        }
    }
}