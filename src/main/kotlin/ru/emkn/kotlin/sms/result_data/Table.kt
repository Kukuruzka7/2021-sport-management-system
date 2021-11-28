package ru.emkn.kotlin.sms.result_data

import logger
import ru.emkn.kotlin.sms.athlete.AthleteNumber

class Table(_map: Map<AthleteNumber, List<CheckpointRes>?>) {
    val map: Map<AthleteNumber, List<CheckpointRes>?>
    operator fun get(athleteNumber: AthleteNumber) = map[athleteNumber]

    init {
        logger.info { "Создание экземпляра класса Table" }
        //заменяем в _map некорректные строчки на нуллы
        map = _map.mapValues { checkAthleteResult(it.value) }
    }

    //проверяем время атлета на чекпоинтах; если они некорректные (потратил отрицательное время на прогон), возвращаем null,
    //если приняли null, то тоже возвращаем null
    private fun checkAthleteResult(athleteResult: List<CheckpointRes>?): List<CheckpointRes>? {
        logger.trace { "checkAthleteResult(athleteResult.size = ${athleteResult?.size}" }
        if (athleteResult == null) {
            return null
        }
        for (i in 0 until athleteResult.lastIndex) {
            if (athleteResult[i].date > athleteResult[i + 1].date) {
                return null
            }
        }
        return athleteResult
    }
}