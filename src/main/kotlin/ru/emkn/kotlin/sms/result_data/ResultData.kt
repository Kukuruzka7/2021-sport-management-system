package ru.emkn.kotlin.sms.result_data

import ru.emkn.kotlin.sms.AthleteNumber

data class ResultData(val table: Table, val startTime: Map<AthleteNumber, CheckpointRes>)