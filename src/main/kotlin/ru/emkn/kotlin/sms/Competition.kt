package ru.emkn.kotlin.sms

import kotlinx.datetime.LocalDate
import logger
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

data class MetaInfo(val name: String, val date: LocalDate, val sport: SportType) {
    override fun toString() = "[$name, $date $sport]"
}

class Competition {
    lateinit var info: MetaInfo
    val teamList: List<Team>
    val athleteList: List<Athlete>
    val groupList: List<Group>
    val athleteByNumber: Map<AthleteNumber, Athlete>

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
    }

    constructor(_info: MetaInfo, application: Application) {
        logger.info { "Вызов конструктора Competition(_info = ${_info}, application)" }
        info = _info
        teamList = application.teamApplicationsList.map { it.team }
        athleteList = teamList.flatMap { it.athletes }
        athleteByNumber = athleteList.associateBy({ it.number }, { it })
        groupList = groupDivision(athleteList)
    }

    constructor(data: CompetitionData) {
        logger.info { "Вызов конструктора Competition(data)" }
        athleteList = data.toAthletesList()
        teamList = generateTeamListByAthleteList(athleteList)
        groupList = generateGroupListByAthleteList(athleteList)
        athleteByNumber = athleteList.associateBy({ it.number }, { it })
    }

    fun getCheckPoint(groupName: GroupName): List<Checkpoint> = TODO()

    fun toCompetitionData() {
        logger.info { "Вызов функции toCompetitionData()" }
        CompetitionData(athleteList.map { athlete ->
            (CompetitionData.Companion.Fields.values().map { athlete.extractFieldToString(it) })
        })
    }

}

private fun groupDivision(athleteList: List<Athlete>): List<Group> {
    logger.trace { "Вызов функции groupDivision(athleteList.size = ${athleteList.size})" }
    val groupNameList = athleteList.map { it.groupName.groupName }.toSet().toList()
    val athleteByGroupName = athleteList.groupBy { it.groupName.groupName }
    return groupNameList.map {
        Group(
            Race(GroupName(it)),
            athleteByGroupName[it] ?: throw WeHaveAProblem("Тут не должно быть null.")
        )
    }
}
