package ru.emkn.kotlin.sms.finishprotocol

import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.athlete.Category
import java.time.LocalDateTime
import java.time.Year

class TeamProtocol(val team: Team, map: Map<Athlete, AthleteProtocol?>) {
    val protocol = map.filter { team.athletes.contains(it.key) }
    val toCSV: Any = TODO()
}

class GroupProtocol(val group: Group, map: Map<Athlete, AthleteProtocol?>) {
    val protocol = map.filter { group.athletes.contains(it.key) }
    val toCSV: Any = TODO()
}

data class AthleteResult(
    val number: AthleteNumber,
    val name: Name,
    val year: Int,
    val category: Category,
    val finishTime: LocalDateTime?,
)

data class AthleteProtocol(
    val result: AthleteResult?,
    val place: Int
)

class FinishProtocol(private val table: Table, competition: Competition) {

    companion object {
        const val dir = "src/main/resources/competitions/"
    }

    private val athleteResult: Map<Athlete, AthleteResult?> =
        competition.athleteList.associateWith { makeIndividualResults(it) }
    private val athleteProtocol: Map<Athlete, AthleteProtocol?> = makeSortedResults(athleteResult)
    private val groupProtocol: Map<Group, GroupProtocol?> = toGroupProtocol(athleteProtocol)
    private val teamProtocols: Map<Team, TeamProtocol?> = TODO()

    val csvByTeams = generateCSVbyTeams()
    val csvByGroups = generateCSVbyGroups()
    val overallCSV = generateOverallCSV()

    private fun generateCSVbyTeams(): Any = TODO()
    private fun generateCSVbyGroups(): Any = TODO()
    private fun generateOverallCSV(): Any = TODO()

    private fun makeIndividualResults(athlete: Athlete): AthleteResult {
        //Время начала и конца путешествия одного чела
        val startTime = table.startTime[athlete.number]
        val finishTime = table[athlete.number]?.get(FinishCheckPoint)?.date
        //Очень сильно просим, чтобы чел начал дистанцию
        require(startTime != null) { "Нет стартового времени у чела под номером ${athlete.number}" }
        return AthleteResult(
            athlete.number,
            athlete.name,
            athlete.birthDate.year,
            athlete.sportCategory,
            finishTime - startTime,
        )
    }

    private fun makeSortedResults(athleteResult: Map<Athlete, AthleteResult?>): Map<Athlete, AthleteProtocol?> {
        val sortedList = athleteResult.toList().sortedBy { it.second?.finishTime }
        return sortedList.associate { it.first to AthleteProtocol(it.second, sortedList.indexOf(it) + 1) }
    }

    private fun toGroupProtocol(athleteProtocol: Map<Athlete, AthleteProtocol?>): Map<Group, GroupProtocol?> {
        val groups = Competition.generateGroupListByAthleteList(athleteProtocol.keys.toList())
        return groups.associateWith { GroupProtocol(it, athleteProtocol) }
    }

    private fun toTeamProtocol(athleteProtocol: Map<Athlete, AthleteProtocol?>): Map<Team, TeamProtocol?> {
        val teams = Competition.generateTeamListByAthleteList(athleteProtocol.keys.toList())
        return teams.associateWith { TeamProtocol(it, athleteProtocol) }
    }
}

//Функция разности двух LocalDateTime
operator fun LocalDateTime?.minus(start: LocalDateTime): LocalDateTime? {
    return this?.minusHours(start.hour.toLong())?.minusMinutes(start.minute.toLong())
        ?.minusSeconds(start.second.toLong())
}