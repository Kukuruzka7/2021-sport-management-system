package ru.emkn.kotlin.sms.model

import logger
import ru.emkn.kotlin.sms.Group
import ru.emkn.kotlin.sms.Race
import ru.emkn.kotlin.sms.WeHaveAProblem
import ru.emkn.kotlin.sms.model.application.Application
import ru.emkn.kotlin.sms.model.athlete.Athlete
import ru.emkn.kotlin.sms.model.athlete.AthleteNumber
import ru.emkn.kotlin.sms.model.athlete.toStringList
import ru.emkn.kotlin.sms.model.result_data.Checkpoint


class Competition {
    val info: MetaInfo
    val teamList: List<Team>
    val athleteList: List<Athlete>
    val groupList: List<Group>
    val athleteByNumber: Map<AthleteNumber, Athlete>
    val checkPointsByGroupName: Map<String, List<Checkpoint>>

    companion object {
        //private
        fun generateTeamListByAthleteList(athList: List<Athlete>): List<Team> {
            logger.trace { "Вызов generateTeamListByAthleteList(athList.size = ${athList.size})" }
            val teamMap = athList.groupBy { it.teamName }
            return teamMap.map { Team(it.key, it.value) }
        }

        //private
        fun generateGroupListByAthleteList(athList: List<Athlete>, sportType: SportType): List<Group> {
            logger.trace { "Вызов generateGroupListByAthleteList(athList.size = ${athList.size})" }
            val groupMap = athList.groupBy { it.race.groupName }
            return groupMap.map { Group(Race((it.key), sportType), it.value) }
        }

        //private
        fun groupDivision(athleteList: List<Athlete>, sportType: SportType): List<Group> {
            logger.trace { "Вызов функции groupDivision(athleteList.size = ${athleteList.size})" }
            val groupNameList = athleteList.map { it.groupName }.toSet().toList()
            val athleteByGroupName = athleteList.groupBy { it.groupName }
            return groupNameList.map {
                Group(
                    Race(it, sportType),
                    athleteByGroupName[it] ?: throw WeHaveAProblem("Тут не должно быть null.")
                )
            }
        }
    }

    constructor(_info: MetaInfo, application: Application) {
        logger.info { "Вызов конструктора Competition(info,data)" }
        info = _info
        teamList = application.teamApplications.map { it.team }
        athleteList = teamList.flatMap { it.athletes }
        athleteByNumber = athleteList.associateBy({ it.number }, { it })
        groupList = groupDivision(athleteList, info.sport)
        checkPointsByGroupName = groupList.associateBy({ it.race.groupName }, { it.race.checkPoints })
    }

    constructor(serial: CompetitionSerialization) {
        logger.info { "Вызов конструктора Competition(data)" }
        info = MetaInfo(serial.metaInfo)
        athleteList = serial.toAthletesList()
        teamList = generateTeamListByAthleteList(athleteList)
        groupList = generateGroupListByAthleteList(athleteList, info.sport)
        athleteByNumber = athleteList.associateBy({ it.number }, { it })
        checkPointsByGroupName = groupList.associateBy({ it.race.groupName }, { it.race.checkPoints })
    }


    fun toCompetitionSerialization(): CompetitionSerialization {
        logger.info { "Вызов функции toCompetitionData()" }
        return CompetitionSerialization(athleteList.map { it.toStringList() }, info.toStringList())
    }

    fun getCheckPoint(groupName: String): List<String> {
        val result = checkPointsByGroupName[groupName]
        require(result != null)
        return result.map { it.name }
    }
}
