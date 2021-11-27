package ru.emkn.kotlin.sms.input_result

import kotlinx.datetime.LocalDateTime
import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.result_data.Checkpoint
import ru.emkn.kotlin.sms.result_data.CheckpointRes


//информация о прохождении атлетом чекпоинтов, создается по названию файла с цсв-шкой.
class InputAthleteResults(override val list: List<String>) : InputResult() {
    //каждому чекпоинту сопоставляет результат данного атлета на нем
    val resultsOnCheckPoints: List<CheckpointRes>
    val number = getAthleteNumber()

    init {
        //отбрасываем первый элемент -- там лежит только название чекпоинта
        val checkPointsResults = list.drop(0) //надо избавиться от первой строчки, там хранился только номер атлета
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
        if (list.isEmpty()) {
            throw ResultMissesAthleteNumber(list)
        }
        return AthleteNumber(list[0])
    }

    //парсим результат на конкретном чекпоинте
    private fun parseResultOnCheckpoint(row: List<String>, athleteNumber: AthleteNumber): CheckpointRes {
        //если данных меньше о прохождении чекпоинта меньше (по количеству), чем ожидаем, мы вылетаем
        if (row.size < Fields.values().size) {
            throw ResultByAthleteInvalidRow(row)
        }
        //парсим время прохождения на чекпоинте
        val time = try {
            LocalDateTime.parse(row[Fields.TIME_ON_CHECKPOINT.ordinal])
        } catch (_: java.time.format.DateTimeParseException) {
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