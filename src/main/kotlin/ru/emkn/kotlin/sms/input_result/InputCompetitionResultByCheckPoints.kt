package ru.emkn.kotlin.sms.input_result

import ru.emkn.kotlin.sms.*


class InputCompetitionResultByCheckPoints(fileNames: List<String>) : InputCompetitionResult {
    val resultsOnPoints = fileNames.map { InputCheckpointResults(it) }
    override fun toTable(): Table {
        val listOfCheckPointRes = resultsOnPoints.flatMap { it.resultsOfAthletes.values }
        return Table(listToMapOfMaps(listOfCheckPointRes))
    }

    private fun listToMapOfMaps(list: List<CheckPointRes>): Map<AthleteNumber, Map<CheckPoint, CheckPointRes>> =
        list.groupBy { it.athleteNumber }.mapValues { listToMap(it.value) }

    private fun listToMap(list: List<CheckPointRes>): Map<CheckPoint, CheckPointRes> =
        list.groupBy { it.checkPoint }.mapValues { it.value[0] }
}