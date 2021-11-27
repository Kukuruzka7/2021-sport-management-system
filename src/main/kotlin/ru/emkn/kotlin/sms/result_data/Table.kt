package ru.emkn.kotlin.sms

import ru.emkn.kotlin.sms.result_data.Checkpoint
import ru.emkn.kotlin.sms.result_data.CheckpointRes
import java.time.LocalDateTime

class Table(val map: Map<AthleteNumber, List<CheckpointRes>>) {
    operator fun get(athleteNumber: AthleteNumber) = map[athleteNumber]
}