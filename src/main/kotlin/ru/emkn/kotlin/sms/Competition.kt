package ru.emkn.kotlin.sms

import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.application.Application
import ru.emkn.kotlin.sms.athlete.Athlete
import ru.emkn.kotlin.sms.athlete.AthleteNumber
import ru.emkn.kotlin.sms.result_data.Checkpoint

enum class SportType(val sportType: String) {
    RUNNING("running"), ERROR("sport is not supported");

    companion object {
        fun getSportType(str: String): SportType {
            for (value in values()) {
                if (value.sportType == str.lowercase()) {
                    return value
                }
            }
            return ERROR
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
    val checkPointsByGroupName: Map<String, List<Checkpoint>>

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
        groupList = groupDivision(athleteList)
        checkPointsByGroupName = groupList.associateBy({ it.race.groupName.groupName }, { it.race.checkPoints })
    }

    constructor(data: CompetitionData) {
        athleteList = data.toAthletesList()
        teamList = generateTeamListByAthleteList(athleteList)
        groupList = generateGroupListByAthleteList(athleteList)
        athleteByNumber = athleteList.associateBy({ it.number }, { it })
        checkPointsByGroupName = groupList.associateBy({ it.race.groupName.groupName }, { it.race.checkPoints })
    }

    fun toCompetitionData() = CompetitionData(athleteList.map { athlete ->
        (CompetitionData.Companion.Fields.values().map { athlete.extractFieldToString(it) })
    })

    fun getCheckPoint(groupName: GroupName): List<Checkpoint> {
        val result = checkPointsByGroupName[groupName.groupName]
        require(result != null)
        return result
    }
}

private fun groupDivision(athleteList: List<Athlete>): List<Group> {
    val groupNameList = athleteList.map { it.groupName.groupName }.toSet().toList()
    val athleteByGroupName = athleteList.groupBy { it.groupName.groupName }
    return groupNameList.map {
        Group(
            Race(GroupName(it)),
            athleteByGroupName[it] ?: throw WeHaveAProblem("Тут не должно быть null.")
        )
    }
}
