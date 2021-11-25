package ru.emkn.kotlin.sms.finishprotocol

import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.athlete.Category
import java.time.LocalDateTime

class TeamProtocol(private val team: Team, val protocol: Map<Athlete, AthleteProtocol?>) {
    val toCSV: Any = TODO()
}

class GroupProtocol(private val group: Group, val protocol: Map<Athlete, AthleteProtocol?>) {
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

    private val path = dir + competition.info.name

    private val athleteResult: Map<Athlete, AthleteResult?> =
        competition.athleteList.associateWith { makeIndividualResults(it) }
    private val groupProtocol: Map<Group, GroupProtocol?> = makeSortedResults(athleteResult)
    private val athleteProtocol: Map<Athlete, AthleteProtocol?> = makeOverallResultsInGroup(groupProtocol)
    private val teamProtocols: Map<Team, TeamProtocol?> = toTeamProtocol(athleteProtocol)

    val csvByTeams = generateCSVbyTeams()
    val csvByGroups = generateCSVbyGroups()
    val overallCSV = generateOverallCSV()

    private fun generateOverallCSV(): Any = TODO()
    private fun generateCSVbyGroups(): Any = TODO()
    private fun generateCSVbyTeams(): Any = TODO()

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

    //
    private fun makeSortedResults(athleteResult: Map<Athlete, AthleteResult?>): Map<Group, GroupProtocol?> {
        val groups = Competition.generateGroupListByAthleteList(athleteResult.keys.toList())
        return groups.associateWith { GroupProtocol(it, makeSortedResultsInGroup(it, athleteResult)) }
    }

    private fun makeSortedResultsInGroup(
        group: Group,
        athleteResult: Map<Athlete, AthleteResult?>
    ): Map<Athlete, AthleteProtocol?> {
        val sortedList =
            athleteResult.filter { group.athletes.contains(it.key) }.toList().sortedBy { it.second?.finishTime }
        return sortedList.associate { it.first to AthleteProtocol(it.second, sortedList.indexOf(it) + 1) }
    }

    private fun makeOverallResultsInGroup(groupProtocol: Map<Group, GroupProtocol?>): Map<Athlete, AthleteProtocol?> {
        return groupProtocol.values.fold(emptyMap()) { initial, it -> initial + (it?.protocol ?: emptyMap()) }
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