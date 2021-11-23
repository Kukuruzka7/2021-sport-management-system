package ru.emkn.kotlin.sms

import java.util.Date

enum class SportType {
    RUNNING
}

data class MetaInfo(val name: String, val date: Date, val sport: SportType)

class Competition {
    lateinit var info: MetaInfo
    lateinit var teamList: List<Team>
    lateinit var athleteList: List<Athlete>
    lateinit var groupList: List<Group>
    lateinit var numerator: Map<Athlete, AthleteNumber>
    lateinit var athleteByNumber: Map<Athlete, Number>

    constructor(application: Application) {
    }

    constructor(data: CompetitionData) {

    }

    fun toCompetitionData(): CompetitionData {
        return TODO()
    }
}