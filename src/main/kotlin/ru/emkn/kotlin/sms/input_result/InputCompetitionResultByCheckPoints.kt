package ru.emkn.kotlin.sms.input_result

import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.result_data.Checkpoint
import ru.emkn.kotlin.sms.result_data.CheckpointRes
import ru.emkn.kotlin.sms.result_data.Table


class InputCompetitionResultByCheckPoints(fileNames: List<String>) : InputCompetitionResult() {
    val resultsOnPoints = fileNames.map { InputCheckpointResults(it) }
    override fun toTable(): Table {
        val listOfCheckPointRes = resultsOnPoints.flatMap { it.resultsOfAthletes.values }
        return Table(TODO())
    }

    private fun listToMapOfMaps(list: List<CheckpointRes>): Map<AthleteNumber, Map<Checkpoint, CheckpointRes>> =
        list.groupBy { it.athleteNumber }.mapValues { listToMap(it.value) }

    private fun listToMap(list: List<CheckpointRes>): Map<Checkpoint, CheckpointRes> =
        list.groupBy { it.checkpoint }.mapValues { it.value[0] }
}