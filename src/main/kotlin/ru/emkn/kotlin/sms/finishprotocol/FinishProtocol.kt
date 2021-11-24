package ru.emkn.kotlin.sms.finishprotocol

import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.athlete.Category
import java.time.LocalDateTime
import java.time.Year

class TeamProtocol {
    val toCSV: Any = TODO()
}

class GroupProtocol {
    val toCSV: Any = TODO()
}

data class AthleteProtocol(
    val place: Int,
    val number: AthleteNumber,
    val name: Name,
    val year: Year,
    val category: Category,
    val finishTime: LocalDateTime
)

class FinishProtocol(table: Table, competition: Competition) {
    private val groupProtocol: Map<Group, GroupProtocol?> = TODO()
    private val teamProtocols: Map<Team, TeamProtocol?> = TODO()
    private val athleteProtocol: Map<Athlete, AthleteProtocol?> = TODO()

    val csvByTeams = generateCSVbyTeams()
    val csvByGroups = generateCSVbyGroups()
    val overallCSV = generateOverallCSV()

    private fun generateCSVbyTeams(): Any = TODO()
    private fun generateCSVbyGroups(): Any = TODO()
    private fun generateOverallCSV(): Any = TODO()

    init {
        athleteProtocol = competition.athleteList.associateWith { makeIndividualProtocol(it, table[it.number]) }
    }

    private fun makeIndividualProtocol (athlete: Athlete, map: Map<CheckPoint, CheckPointRes>?): AthleteProtocol? {
        if(map == null) {
            return null
        }
        return null
    }
}