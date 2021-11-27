package ru.emkn.kotlin.sms.input_result

import ru.emkn.kotlin.sms.InputCompetitionResult
import ru.emkn.kotlin.sms.result_data.Table


//
class InputCompetitionResultByAthletes(fileNames: List<String>) : InputCompetitionResult() {
    val resultsOnPoints = fileNames.map { InputAthleteResults(it) }
    override fun toTable() = Table(resultsOnPoints.associateBy({ it.number }, { it.resultsOnCheckPoints }))

}