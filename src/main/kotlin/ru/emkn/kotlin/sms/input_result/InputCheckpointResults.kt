package ru.emkn.kotlin.sms.input_result

import kotlinx.datetime.LocalDateTime
import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.result_data.Checkpoint
import ru.emkn.kotlin.sms.result_data.CheckpointRes

class InputCheckpointResults(override val fileName: String) : InputResult {
    //каждому атлету сопоставляет его результат на данном чекпоинте
    val resultsOfAthletes: Map<AthleteNumber, CheckpointRes>

    //этот тот чувак, о котором собственно идет речь
    val checkpoint: Checkpoint

    init {
        val rows = getRows()
        val athletesResults = rows.drop(0)
        checkpoint = getCheckPoint(athletesResults)
        resultsOfAthletes =
            athletesResults.map { parseResultOfAthlete(it, checkpoint) }.associateBy({ it.athleteNumber }, { it })
    }

    companion object {
        val CHECKPOINT_RESULT_FORMAT =
            mapOf(Fields.ATHLETE_NUMBER to "athlete number", Fields.ATHLETE_TIME to "athlete time")

        enum class Fields {
            ATHLETE_NUMBER, ATHLETE_TIME
        }
    }

    private fun getCheckPoint(rows: List<List<String>>): Checkpoint {
        if (rows.isEmpty() || rows[0].isEmpty()) {
            throw ResultMissesCheckPointName(fileName)
        }
        return Checkpoint(rows[0][0])
    }

    //парсит результат конкретного атлета
    private fun parseResultOfAthlete(list: List<String>, _checkpoint: Checkpoint): CheckpointRes {
        //если данных меньше о прохождении атлета меньше (по количеству), чем ожидаем, мы вылета
        if (list.size < Fields.values().size) {
            throw ResultByCheckpointInvalidRow(fileName, list)
        }
        //парсим время прохождения атлета
        val time = try {
            LocalDateTime.parse(list[Fields.ATHLETE_TIME.ordinal])
        } catch (_: java.time.format.DateTimeParseException) {
            throw InvalidDateFormat(fileName, list[Fields.ATHLETE_TIME.ordinal])
        }
        TODO("попарсить locadaytime")
        return CheckpointRes(_checkpoint, AthleteNumber(list[Fields.ATHLETE_NUMBER.ordinal]), time)
    }
}
