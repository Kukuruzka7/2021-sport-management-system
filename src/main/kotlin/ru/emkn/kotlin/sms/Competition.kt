package ru.emkn.kotlin.sms

import java.util.Date

enum class SportType {
    RUNNING
}

data class MetaInfo(val name: String, val date: Date, val sport: SportType)

class Competition {
    lateinit var info: MetaInfo
    val teamList: List<Team>
    val athleteList: List<Athlete>
    val groupList: List<Group>
    val athleteByNumber: Map<AthleteNumber, Athlete>


    constructor(application: Application) {
        teamList = application.teamApplicationsList.map { it.team }
        athleteList = teamList.flatMap { it.athleteList }
        athleteByNumber = athleteList.associateBy({ it.number }, { it })
        groupList = groupDivision(athleteList)
    }

    //constructor(data: CompetitionData) {

    //}

    private fun groupDivision(athleteList: List<Athlete>): List<Group> {
        val raceList = athleteList.map { "${it.sex}${it.birthDate}" }.toSet().toList()
        val athleteByRace = athleteList.groupBy { "${it.sex}${it.birthDate}" }
        val result = raceList.map { Group(Race(it)) }
        result.forEach {
            assert(athleteByRace[it.race.groupName] != null)
            it.athletes = athleteByRace[it.race.groupName] ?: throw WeHaveAProblem("Тут не должно быть null.")
            athleteByRace[it.race.groupName]?.forEach { athlete ->
                athlete.group = it
            } ?: throw WeHaveAProblem("Тут не должно быть null.")
        }
        return result
    }

    fun toCompetitionData(): CompetitionData {
        return TODO()
    }
}