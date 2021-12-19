package ru.emkn.kotlin.sms.model.input_result

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import logger
import ru.emkn.kotlin.sms.InputCheckpointResultIsAbsent
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.athlete.Athlete
import ru.emkn.kotlin.sms.model.result_data.CheckpointRes
import ru.emkn.kotlin.sms.model.result_data.Table
import java.io.File


class InputCompetitionResultByCheckPoints(override val rows: List<List<String>>, private val competition: Competition) :
    InputCompetitionResult() {
    constructor(fileName: String, competition: Competition) : this(
        csvReader().readAll(File(fileName).readText()),
        competition
    )

    //по названию чекпоинта получаем результат атлетов на этом чекпоинте, достается из competition
    private val checkpointNameMap: Map<String, InputCheckpointResults> = buildCheckpointMap()

    private fun buildCheckpointMap(): Map<String, InputCheckpointResults> {
        logger.trace { "Вызов buildCheckpointMap()" }
        val resultsOnPoints = rows.map { InputCheckpointResults(it) } //по названию файла делаем класс
        return resultsOnPoints.associateBy { it.checkpoint.name } //строим мапу
    }

    override fun toTable(): Table =
    //строим мапу по атлетам, ключ -- номер атлета, значение -- список из результатов по чекпоинтам этого атлета
        //(или нулл, если результаты некорректны)
        Table(competition.athleteList.associateBy({ it.number }, { getCheckpointResList(it) }))

    //дает список пройденных атлетом названий чекпоинтов
    private fun getCheckpoints(athlete: Athlete): List<String> = competition.getCheckPoint(athlete.groupName)

    //возвращает список результатов на чекпоинтах у данного атлета, и null, если атлет не прошел какой-то чекпоинт
    private fun getCheckpointResList(athlete: Athlete): List<CheckpointRes>? {
        logger.trace { "Вызов getCheckpointResList(athlete = ${athlete})" }
        checkIfAllCheckpointArePresent(athlete)
        val checkpoints = getCheckpoints(athlete) //список чекпоинта данного молодого человека
        //если результата атлета на каком-то из чекпоинтов нет, возвращаем null
        if (checkpoints.any { checkpointNameMap[it]!!.resultsOfAthletes[athlete.number] == null }) {
            return null
        }
        //список из данных о том, как атлет эти чекпоинты прошел (возможны нуллы, если атлет не был на каких-то чекпоинтах)
        return checkpoints.map { checkpointNameMap[it]!!.resultsOfAthletes[athlete.number]!! }
    }

    //проверяет, есть ли у нас результаты по всем нужны чекпоинтам
    private fun checkIfAllCheckpointArePresent(athlete: Athlete) {
        logger.trace { "Вызов checkIfAllCheckpointArePresent(athlete = $athlete)" }
        //получаем все чекпоинты, который данный атлет по идее должен пройти
        val checkpoints = getCheckpoints(athlete)
        checkpoints.forEach { checkpoint ->
            //получаем результаты атлетов на данном чекпоинте, кидаем исключение, если нам такой не дали
            if (checkpointNameMap[checkpoint] == null) {
                logger.error { InputCheckpointResultIsAbsent(checkpoint) }
                throw  InputCheckpointResultIsAbsent(checkpoint)
            }
        }
    }
}