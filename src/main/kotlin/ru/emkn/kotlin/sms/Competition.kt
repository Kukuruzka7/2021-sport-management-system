package ru.emkn.kotlin.sms

import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.application.Application
import ru.emkn.kotlin.sms.athlete.Athlete
import ru.emkn.kotlin.sms.athlete.AthleteNumber
import ru.emkn.kotlin.sms.result_data.Checkpoint

enum class SportType(val sportType: String) {
    RUNNING("бег"), ERR("спорт который мы не поддерживаем");

    companion object {
        fun getSportTypeFromString(str: String): SportType {
            for (value in values()) {
                if (value.sportType == str.lowercase()) {
                    return value
                }
            }
            return ERR
        }
    }
}

data class MetaInfo(val name: String, val date: LocalDate, val sport: SportType)

class Competition {
    lateinit var info: MetaInfo
    val teamList: List<Team>
    val athleteList: List<Athlete>
    val groupList: List<Group>
    val athleteByNumber: Map<AthleteNumber, Athlete>

    companion object {
        private fun generateTeamListByAthleteList(athList: List<Athlete>): List<Team> {
            val teamMap = athList.groupBy { it.teamName }
            return teamMap.keys.map { Team(it, teamMap[it]!!) }
        }

        private fun generateGroupListByAthleteList(athList: List<Athlete>): List<Group> {
            val groupMap = athList.groupBy { it.race }
            return groupMap.keys.map { Group(it, groupMap[it]!!) }
        }
    }

    constructor(_info: MetaInfo, application: Application) {
        info = _info
        teamList = application.teamApplicationsList.map { it.team }
        athleteList = teamList.flatMap { it.athletes }
        athleteByNumber = athleteList.associateBy({ it.number }, { it })
        groupList = groupDivision(athleteList, _info.sport)
    }

    constructor(data: CompetitionData) {
        athleteList = data.toAthletesList()
        teamList = generateTeamListByAthleteList(athleteList)
        groupList = generateGroupListByAthleteList(athleteList)
        athleteByNumber = athleteList.associateBy({ it.number }, { it })
    }

    fun getCheckPoint(groupName: GroupName): List<Checkpoint> = TODO()

    fun toCompetitionData() = CompetitionData(athleteList.map { athlete ->
        (CompetitionData.Companion.Fields.values().map { athlete.extractFieldToString(it) })
    })
}

private fun groupDivision(athleteList: List<Athlete>, sportType: SportType): List<Group> {
    val groupNameList = athleteList.map { it.groupName }.toSet().toList()
    val athleteByGroupName = athleteList.groupBy { it.groupName }
    return groupNameList.map {
        Group(
            Race(it, sportType),
            athleteByGroupName[it] ?: throw WeHaveAProblem("Тут не должно быть null.")
        )
    }
}
