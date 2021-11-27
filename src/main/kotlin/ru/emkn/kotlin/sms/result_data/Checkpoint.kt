package ru.emkn.kotlin.sms.result_data


import kotlinx.datetime.LocalDateTime
import ru.emkn.kotlin.sms.AthleteNumber


class Checkpoint(val name: String)

open class CheckpointRes(open val checkpoint: Checkpoint, val athleteNumber: AthleteNumber, val date: LocalDateTime)

