package ru.emkn.kotlin.sms.result_data

import ru.emkn.kotlin.sms.AthleteNumber
import ru.emkn.kotlin.sms.InputCompetitionResult
import ru.emkn.kotlin.sms.Table
import java.time.LocalDateTime

data class ResultData(val Table: Table, val startTime: Map<AthleteNumber, LocalDateTime>) {
    constructor(inputCompetitionResult: InputCompetitionResult, startTime: Map<AthleteNumber, LocalDateTime>) :
            this(inputCompetitionResult.toTable(), startTime)

}