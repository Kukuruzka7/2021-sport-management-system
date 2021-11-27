package ru.emkn.kotlin.sms.result_data

import kotlinx.datetime.LocalDateTime
import ru.emkn.kotlin.sms.AthleteNumber
import ru.emkn.kotlin.sms.InputCompetitionResult
import java.time.LocalTime

data class ResultData(val table: Table, val startTime: Map<AthleteNumber, LocalTime>) {
    constructor(inputCompetitionResult: InputCompetitionResult, startTime: Map<AthleteNumber, LocalTime>) :
            this(inputCompetitionResult.toTable(), startTime)

}