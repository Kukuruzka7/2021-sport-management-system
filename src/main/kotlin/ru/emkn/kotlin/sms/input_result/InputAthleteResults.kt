package ru.emkn.kotlin.sms.input_result

import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.athlete.AthleteNumber
import ru.emkn.kotlin.sms.result_data.Checkpoint
import ru.emkn.kotlin.sms.result_data.CheckpointRes
import java.time.LocalTime
import logger


//информация о прохождении атлетом чекпоинтов, создается по названию файла с цсв-шкой.
class InputAthleteResults(_list: List<String>) : InputResult() {
    //каждому чекпоинту сопоставляет результат данного атлета на нем
    override val list: List<String> = _list.dropLastWhile { it.isEmpty() }
    val resultsOnCheckPoints: List<CheckpointRes>
    val number = getAthleteNumber()

    init {
        logger.trace { "Инит-блок класса InputAthleteResults(_list.size = ${list.size})" }
        //отбрасываем первый элемент -- там лежит только название чекпоинта
        val checkPointsResults = list.drop(1) //надо избавиться от первой строчки, там хранился только номер атлета
        //количество полей, которое описывает результаты каждого атлета
        val numOfFields = Fields.values().size
        //представляем линейный массив в виде прямоугольного, в каждой строке -- информация атлетом
        val splitByAthletes: List<List<String>> = List(checkPointsResults.size / numOfFields) { i ->
            checkPointsResults.subList(numOfFields * i, numOfFields * (i + 1))
        }
        resultsOnCheckPoints =
            splitByAthletes.map { parseResultOnCheckpoint(it, number) }
    }

    //извлекает номер
    private fun getAthleteNumber(): AthleteNumber {
        logger.trace { "Вызов getAthleteNumber()" }
        if (list.isEmpty()) {
            logger.error { ResultMissesAthleteNumber(list) }
            throw ResultMissesAthleteNumber(list)
        }
        return AthleteNumber(list[0])
    }

    //парсим результат на конкретном чекпоинте
    private fun parseResultOnCheckpoint(row: List<String>, athleteNumber: AthleteNumber): CheckpointRes {
        logger.trace { "Вызов parseResultOnCheckpoint(row.size = ${row.size}, athleteNumber = $athleteNumber" }
        //если данных меньше о прохождении чекпоинта меньше (по количеству), чем ожидаем, мы вылетаем
        if (row.size < Fields.values().size) {
            logger.error { ResultByAthleteInvalidRow(row) }
            throw ResultByAthleteInvalidRow(row)
        }
        //парсим время прохождения на чекпоинте
        val time = try {
            LocalTime.parse(row[Fields.TIME_ON_CHECKPOINT.ordinal])
        } catch (_: Exception) {
            logger.error { InvalidDateFormat(row[Fields.TIME_ON_CHECKPOINT.ordinal]) }
            throw InvalidDateFormat(row[Fields.TIME_ON_CHECKPOINT.ordinal])
        }
        //Fields.CHECKPOINT_NAME.ordinal -- это номер, под которым в list содержится нужная строчка
        return CheckpointRes(Checkpoint(row[Fields.CHECKPOINT_NAME.ordinal]), athleteNumber, time)
    }


    companion object {
        //их названия на человеческом, для вывода ошибок
        val ATHLETE_RESULT_FORMAT =
            mapOf(Fields.CHECKPOINT_NAME to "checkpoint name", Fields.TIME_ON_CHECKPOINT to "time on checkpoint")

        //какие поля хранит себе информация о прохождении на чекпоинте
        enum class Fields {
            CHECKPOINT_NAME, TIME_ON_CHECKPOINT
        }
    }
}