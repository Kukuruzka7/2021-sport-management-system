package ru.emkn.kotlin.sms

import java.time.LocalDateTime

object FinishCheckPoint: CheckPoint("Finish")

open class CheckPoint(val name: String)

class CheckPointRes(val checkPoint: CheckPoint, val athleteNumber: AthleteNumber, val date: LocalDateTime)

class Table(val map: Map<AthleteNumber, Map<CheckPoint, CheckPointRes>>, val startTime: Map<AthleteNumber, LocalDateTime>) {
    operator fun get(athleteNumber: AthleteNumber) = map[athleteNumber]
}