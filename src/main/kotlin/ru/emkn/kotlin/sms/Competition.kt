package ru.emkn.kotlin.sms

import ru.emkn.kotlin.sms.application.Application
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

    constructor(data: CompetitionData) {
        athleteList = data.toAthletesList()
        teamList = generateTeamListByAthleteLeast(athleteList)
        groupList = generateGroupListByAthleteLeast(athleteList)
        athleteByNumber = athleteList.associateBy({ it.number }, { it })
    }

    fun toCompetitionData() = CompetitionData(athleteList.map { athlete ->
        (CompetitionData.Companion.Fields.values().map { athlete.extractFieldToString(it) })
    })
}

    private fun groupDivision(athleteList: List<Athlete>): List<Group> {
        val groupNameList = athleteList.map { it.groupName }.toSet().toList()
        val athleteByGroupName = athleteList.groupBy { it.groupName }
        return groupNameList.map {
            Group(
                Race(it),
                athleteByGroupName[it] ?: throw WeHaveAProblem("Тут не должно быть null.")
            )
        }
    }

private fun generateTeamListByAthleteLeast(athList: List<Athlete>): List<Team> {
    val teamMap = athList.groupBy { it.teamName }
    return teamMap.keys.map { Team(it, teamMap[it]!!) }
}

private fun generateGroupListByAthleteLeast(athList: List<Athlete>): List<Group> {
    val groupMap = athList.groupBy { it.race }
    return groupMap.keys.map { Group(it, groupMap[it]!!) }
}