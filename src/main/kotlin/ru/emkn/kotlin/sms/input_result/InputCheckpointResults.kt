package ru.emkn.kotlin.sms.input_result

import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.*

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
        val CHECKPOINT_RESULT_FORMAT =
            mapOf(Fields.ATHLETE_NUMBER to "athlete number", Fields.ATHLETE_TIME to "athlete time")

        enum class Fields {
            ATHLETE_NUMBER, ATHLETE_TIME
        }
    }

    private fun getCheckPoint(rows: List<List<String>>): CheckPoint {
        if (rows.isEmpty() || rows[0].isEmpty()) {
            throw ResultMissesCheckPointName(fileName)
        }
        return CheckPoint(rows[0][0])
    }

    private fun parseRow(row: List<String>, checkPoint: CheckPoint): CheckPointRes {
        if (row.size < Fields.values().size) {
            throw ResultByCheckpointInvalidRow(fileName, row)
        }
        val time = try {
            LocalDate.parse(row[Fields.ATHLETE_TIME.ordinal])
        } catch (_: java.time.format.DateTimeParseException) {
            throw InvalidDateFormat(fileName, row[Fields.ATHLETE_TIME.ordinal])
        }
        return CheckPointRes(checkPoint, AthleteNumber(row[Fields.ATHLETE_NUMBER.ordinal]), time)
    }
}
