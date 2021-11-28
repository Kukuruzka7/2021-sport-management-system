package ru.emkn.kotlin.sms.input_result

import kotlinx.datetime.LocalDateTime
import logger
import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.athlete.AthleteNumber
import ru.emkn.kotlin.sms.result_data.Checkpoint
import ru.emkn.kotlin.sms.result_data.CheckpointRes
import java.time.LocalTime

class InputCheckpointResults(override val list: List<String>) : InputResult() {
    //каждому атлету сопоставляет его результат на данном чекпоинте
    val resultsOfAthletes: Map<AthleteNumber, CheckpointRes>

    //этот тот чекпоинт, о котором собственно идет речь
    val checkpoint: Checkpoint = getCheckPoint()

    init {
        logger.trace { "Инит-блок класса InputCheckpointResults(_list.size = ${list.size})" }
        //отбрасываем первый элемент -- там лежит только название чекпоинта
        val athletesResults = list.drop(0)
        //количество полей, которое описывает результаты каждого атлета
        val numOfFields = Fields.values().size
        //представляем линейный массив в виде прямоугольного, в каждой строке -- информация атлетом
        val splitByAthletes: List<List<String>> = List(athletesResults.size / numOfFields) { i ->
            athletesResults.subList(numOfFields * i, numOfFields * (i + 1))
        }
        resultsOfAthletes =
            splitByAthletes.map { parseResultOfAthlete(it, checkpoint) }.associateBy { it.athleteNumber }
    }

    companion object {
        val CHECKPOINT_RESULT_FORMAT =
            mapOf(Fields.ATHLETE_NUMBER to "athlete number", Fields.ATHLETE_TIME to "athlete time")

        enum class Fields {
            ATHLETE_NUMBER, ATHLETE_TIME
        }
    }

    private fun getCheckPoint(): Checkpoint {
        logger.trace { "Вызов getCheckPoint()" }
        if (list.isEmpty()) {
            throw ResultMissesCheckPointName(list)
        }
        return Checkpoint(list[0])
    }

    //парсит результат конкретного атлета
    private fun parseResultOfAthlete(row: List<String>, _checkpoint: Checkpoint): CheckpointRes {
        logger.trace { "Вызов parseResultOfAthlete(row.size = ${row.size}, _checkpoint = $_checkpoint" }
        //если данных меньше о прохождении атлета меньше (по количеству), чем ожидаем, мы вылета
        if (row.size < Fields.values().size) {
            throw ResultByCheckpointInvalidRow(row)
        }
        //парсим время прохождения атлета
        val time = try {
            LocalTime.parse(row[Fields.ATHLETE_TIME.ordinal])
        } catch (_: java.time.format.DateTimeParseException) {
            throw InvalidDateFormat(row[Fields.ATHLETE_TIME.ordinal])
        }
        return CheckpointRes(_checkpoint, AthleteNumber(row[Fields.ATHLETE_NUMBER.ordinal]), time)
    }
}
