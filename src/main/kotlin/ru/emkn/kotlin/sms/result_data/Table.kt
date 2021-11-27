package ru.emkn.kotlin.sms.result_data

import ru.emkn.kotlin.sms.AthleteNumber

class Table(val map: Map<AthleteNumber, List<CheckpointRes>>) {
    operator fun get(athleteNumber: AthleteNumber) = map[athleteNumber]
}