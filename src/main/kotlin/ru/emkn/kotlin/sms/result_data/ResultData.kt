package ru.emkn.kotlin.sms.result_data

import kotlinx.datetime.LocalDateTime
import ru.emkn.kotlin.sms.AthleteNumber
import ru.emkn.kotlin.sms.InputCompetitionResult

data class ResultData(val table: Table, val startTime: Map<AthleteNumber, LocalDateTime>) {
    constructor(inputCompetitionResult: InputCompetitionResult, startTime: Map<AthleteNumber, LocalDateTime>) :
            this(inputCompetitionResult.toTable(), startTime)

}