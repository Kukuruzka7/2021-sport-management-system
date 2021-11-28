package ru.emkn.kotlin.sms

import logger
import ru.emkn.kotlin.sms.application.Application
import ru.emkn.kotlin.sms.athlete.Athlete
import ru.emkn.kotlin.sms.athlete.AthleteNumber
import ru.emkn.kotlin.sms.result_data.Checkpoint


class Competition {
    lateinit var info: MetaInfo
    val teamList: List<Team>
    val athleteList: List<Athlete>
    val groupList: List<Group>
    val athleteByNumber: Map<AthleteNumber, Athlete>
    val checkPointsByGroupName: Map<String, List<Checkpoint>>

    companion object {
        private fun generateTeamListByAthleteList(athList: List<Athlete>): List<Team> {
            logger.trace { "Вызов generateTeamListByAthleteList(athList.size = ${athList.size}" }
            val teamMap = athList.groupBy { it.teamName }
            return teamMap.keys.map { Team(it, teamMap[it]!!) }
        }

        private fun generateGroupListByAthleteList(athList: List<Athlete>): List<Group> {
            logger.trace { "Вызов generateGroupListByAthleteList(athList.size = ${athList.size}" }
            val groupMap = athList.groupBy { it.race }
            return groupMap.keys.map { Group(it, groupMap[it]!!) }
        }

        private fun groupDivision(athleteList: List<Athlete>): List<Group> {
            logger.trace { "Вызов функции groupDivision(athleteList.size = ${athleteList.size})" }
            val groupNameList = athleteList.map { it.groupName.value }.toSet().toList()
            val athleteByGroupName = athleteList.groupBy { it.groupName.value }
            return groupNameList.map {
                Group(
                    Race(GroupName(it)),
                    athleteByGroupName[it] ?: throw WeHaveAProblem("Тут не должно быть null.")
                )
            }
        }
    }

    constructor(_info: MetaInfo, application: Application) {
        info = _info
        teamList = application.teamApplicationsList.map { it.team }
        athleteList = teamList.flatMap { it.athletes }
        athleteByNumber = athleteList.associateBy({ it.number }, { it })
        groupList = groupDivision(athleteList)
        checkPointsByGroupName = groupList.associateBy({ it.race.groupName.value }, { it.race.checkPoints })
    }

    constructor(data: CompetitionData) {
        logger.info { "Вызов конструктора Competition(data)" }
        athleteList = data.toAthletesList()
        teamList = generateTeamListByAthleteList(athleteList)
        groupList = generateGroupListByAthleteList(athleteList)
        athleteByNumber = athleteList.associateBy({ it.number }, { it })
        checkPointsByGroupName = groupList.associateBy({ it.race.groupName.value }, { it.race.checkPoints })
    }

    fun toCompetitionData(): CompetitionData {
        logger.info { "Вызов функции toCompetitionData()" }
        return CompetitionData(athleteList.map { athlete ->
            (CompetitionData.Companion.Fields.values().map { athlete.extractFieldToString(it) })
        })
    }

    fun getCheckPoint(groupName: GroupName): List<Checkpoint> {
        val result = checkPointsByGroupName[groupName.value]
        require(result != null)
        return result
    }
}
