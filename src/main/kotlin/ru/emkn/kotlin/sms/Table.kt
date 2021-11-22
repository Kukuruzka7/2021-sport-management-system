package ru.emkn.kotlin.sms

class CheckPoint

class PointRes

class Table(val map: Map<AthleteNumber, Map<CheckPoint, PointRes>>) {
    operator fun get(athleteNumber: AthleteNumber) = map[athleteNumber]
}