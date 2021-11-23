package ru.emkn.kotlin.sms

import kotlinx.datetime.LocalDate

class CheckPoint(val name: String)

class CheckPointRes(val checkPoint: CheckPoint, val date: LocalDate)

class Table(val map: Map<AthleteNumber, Map<CheckPoint, CheckPointRes>>) {
    operator fun get(athleteNumber: AthleteNumber) = map[athleteNumber]
}