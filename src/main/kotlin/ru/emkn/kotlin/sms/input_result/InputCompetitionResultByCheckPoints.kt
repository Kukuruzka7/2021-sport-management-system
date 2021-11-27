package ru.emkn.kotlin.sms.input_result

import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.athlete.Athlete
import ru.emkn.kotlin.sms.result_data.Checkpoint
import ru.emkn.kotlin.sms.result_data.CheckpointRes
import ru.emkn.kotlin.sms.result_data.Table


class InputCompetitionResultByCheckPoints(val fileNames: List<String>, val competition: Competition) :
    InputCompetitionResult() {

    //по чекпоинту получаем результат атлетов на этом чекпоинте, достается из competition
    val checkpointMap: Map<Checkpoint, InputCheckpointResults> = buildCheckpointMap()

    private fun buildCheckpointMap(): Map<Checkpoint, InputCheckpointResults> {
        val resultsOnPoints = fileNames.map { InputCheckpointResults(it) } //по названию файла делаем класс
        return resultsOnPoints.associateBy { it.checkpoint } //строим мапу
    }

    override fun toTable(): Table =
    //строим мапу по атлетам, ключ -- номер атлета, значение -- список из результатов по чекпоинтам этого атлета
        //(или нулл, если результаты некорректны)
        Table(competition.athleteList.associateBy({ it.number }, { getCheckpointResList(it) }))

    //дает список пройденных атлетом чекпоинтов
    private fun getCheckpoints(athlete: Athlete): List<Checkpoint> = competition.getCheckPoint(athlete.groupName)

    //возвращает список результатов на чекпоинтах у данного атлета, и null, если атлет не прошел какой-то чекпоинт
    private fun getCheckpointResList(athlete: Athlete): List<CheckpointRes>? {
        val checkpoints = getCheckpoints(athlete) //список чекпоинта данного молодого человека
        //если результата атлета на каком-то из чекпоинтов нет, возвращаем null
        if (checkpoints.any { checkpointMap[it]!!.resultsOfAthletes[athlete.number] == null }) {
            return null
        }
        //список из данных о том, как атлет эти чекпоинты прошел (возможны нуллы, если атлет не был на каких-то чекпоинтах)
        return checkpoints.map { checkpointMap[it]!!.resultsOfAthletes[athlete.number]!! }
    }

    //проверяет, есть ли у нас результаты по всем нужны чекпоинтам
    private fun ifAllCheckpointArePresent(athlete: Athlete) {
        //получаем все чекпоинты, который данный атлет по идее должен пройти
        val checkpoints = getCheckpoints(athlete)
        checkpoints.forEach { checkpoint ->
            //получаем результаты атлетов на данном чекпоинте, кидаем исключение, если нам такой не дали
            if (checkpointMap[checkpoint] == null) {
                throw  InputCheckpointResultIsAbsent(checkpoint)
            }
        }
    }
}