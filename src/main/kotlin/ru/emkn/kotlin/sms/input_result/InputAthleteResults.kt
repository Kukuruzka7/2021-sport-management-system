package ru.emkn.kotlin.sms.input_result

import kotlinx.datetime.LocalDateTime
import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.athlete.AthleteNumber
import ru.emkn.kotlin.sms.result_data.Checkpoint
import ru.emkn.kotlin.sms.result_data.CheckpointRes


//информация о прохождении атлетом чекпоинтов, создается по названию файла с цсв-шкой.
class InputAthleteResults(override val fileName: String) : InputResult {
    //каждому чекпоинту сопоставляет результат данного атлета на нем
    val resultsOnCheckPoints: List<CheckpointRes>
    val number: AthleteNumber

    init {
        val rows = getRows() //см. InputResult
        number = getAthleteNumber(rows) //извлекаем номер
        val checkPointsResults = rows.drop(0) //надо избавиться от первой строчки, там хранился только номер атлета
        resultsOnCheckPoints =
            checkPointsResults.map { parseResultOnCheckpoint(it, number) }
    }

    //извлекает номер
    private fun getAthleteNumber(rows: List<List<String>>): AthleteNumber {
        if (rows.isEmpty() || rows[0].isEmpty()) { //TODO надо бы от ноликов избавиться
            throw ResultMissesAthleteNumber(fileName)
        }
        return AthleteNumber(rows[0][0])
    }

    //парсим результат на конкретном чекпоинте
    private fun parseResultOnCheckpoint(list: List<String>, athleteNumber: AthleteNumber): CheckpointRes {
        //если данных меньше о прохождении чекпоинта меньше (по количеству), чем ожидаем, мы вылетаем
        if (list.size < Fields.values().size) {
            throw ResultByAthleteInvalidRow(fileName, list)
        }
        //парсим время прохождения на чекпоинте
        val time = try {
            LocalDateTime.parse(list[Fields.TIME_ON_CHECKPOINT.ordinal])
        } catch (_: java.time.format.DateTimeParseException) {
            throw InvalidDateFormat(fileName, list[Fields.TIME_ON_CHECKPOINT.ordinal])
        }
        //Fields.CHECKPOINT_NAME.ordinal -- это номер, под которым в list содержится нужная строчка
        return CheckpointRes(Checkpoint(list[Fields.CHECKPOINT_NAME.ordinal]), athleteNumber, time)
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