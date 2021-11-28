package ru.emkn.kotlin.sms.result_data

import ru.emkn.kotlin.sms.athlete.AthleteNumber
import ru.emkn.kotlin.sms.input_result.InputCompetitionResult
import java.time.LocalTime

data class ResultData(val table: Table, val startTime: Map<AthleteNumber, LocalTime>) {
    constructor(
        inputCompetitionResult: InputCompetitionResult,
        _startTime: Map<AthleteNumber, LocalTime>,
    ) :
            this(inputCompetitionResult.toTable(), _startTime)

}