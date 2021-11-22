package ru.emkn.kotlin.sms

import java.util.Date

enum class SportType {
    RUNNING
}

data class MetaInfo(val name: String, val date: Date, val sport: SportType)

class Competition(val info: MetaInfo, val application: Application) {
    val teamList: List<Team> = TODO()
    val athleteList: List<Athlete> = TODO()
    val groupList: List<Group> = TODO()
    val numerator: Map<AthleteNumber, Athlete> = TODO()

}