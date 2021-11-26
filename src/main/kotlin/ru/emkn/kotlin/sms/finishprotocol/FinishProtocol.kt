package ru.emkn.kotlin.sms.finishprotocol

import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.athlete.Category
import java.io.File
import java.time.LocalDateTime

class TeamProtocol(private val team: Team, val protocol: List<AthleteProtocol?>) {
    val toCSV: Any = TODO()
}

class GroupProtocol(private val group: Group, val protocol: List<AthleteProtocol?>) {
    val toCSV: Any = TODO()
}

data class AthleteResult(
    val athlete: Athlete,
    val finishTime: LocalDateTime?,
)

data class AthleteProtocol(
    val athlete: Athlete,
    val finishTime: LocalDateTime?,
    val place: Int
)

class FinishProtocol(private val table: Table, competition: Competition) {

    companion object {
        const val dir = "src/main/resources/competitions/"
    }

    private val path = """$dir${competition.info.name}/finishProtocol/"""

    init {
        if (!File(path).exists()) {
            try {
                File(path).mkdir()
            } catch (_: Exception) {
                throw DirectoryCouldNotBeCreated(path)
            }
        }
    }

    private val athletes = competition.athleteList
    private val groups = competition.groupList
    private val teams = competition.teamList

    private val athleteResult: List<AthleteResult> =
        athletes.map { makeIndividualResults(it) }

    private fun makeIndividualResults(athlete: Athlete): AthleteResult {
        //Время начала и конца путешествия одного чела
        val startTime = table.startTime[athlete.number]
        val finishTime = table[athlete.number]?.get(FinishCheckPoint)?.date
        //Очень сильно просим, чтобы чел начал дистанцию
        require(startTime != null) { "Нет стартового времени у чела под номером ${athlete.number}" }
        return AthleteResult(
            athlete,
            finishTime - startTime,
        )
    }


    //Делает общие списки групп
    private val groupProtocol: List<GroupProtocol> =
        groups.map { GroupProtocol(it, makeSortedResultsInGroup(it, athleteResult)) }

    //Выставляет номера спортсменов в их группе
    private fun makeSortedResultsInGroup(group: Group, athleteResult: List<AthleteResult>): List<AthleteProtocol?> {
        val sortedList = athleteResult.filter { group.athletes.contains(it.athlete) }.sortedBy { it.finishTime }
        return sortedList.map { AthleteProtocol(it.athlete, it.finishTime, sortedList.indexOf(it) + 1) }
    }

    //Объединяет списки по группам в общий список
    private val athleteProtocol: List<AthleteProtocol?> =
        groupProtocol.fold(emptyList()) { initial, it -> initial + it.protocol }

    //Разделяет общий список на списки по командам
    private val teamProtocols: List<TeamProtocol> =
        teams.map { TeamProtocol(it, athleteProtocol) }

    val csvByTeams = generateCSVbyTeams()
    val csvByGroups = generateCSVbyGroups()
    val overallCSV = generateOverallCSV()

    private fun generateCSVbyGroups() {
        val fileName = path + "groups"
        File(fileName).delete()
        File(fileName).mkdir()

    }

    private fun generateOverallCSV() {
        val fileName = path + "overallCSV"
        File(fileName).delete()
        File(fileName).createNewFile()
//        CsvWriter().open(fileName) {
//            writeRow("Общие результаты")
//            //пока без сортировки по номерам, так как номера это стринги и нужно писать компоратор
//            athleteProtocol.values.sortedBy { it.result?.groupName?.groupName }.forEach {
//
//                writeRow(
//                    it.result?.groupName?.groupName,
//
//                    )
//
//            }
//        }
    }

    private fun generateCSVbyTeams(): Any = TODO()

}

//Функция разности двух LocalDateTime
operator fun LocalDateTime?.minus(start: LocalDateTime): LocalDateTime? {
    return this?.minusHours(start.hour.toLong())?.minusMinutes(start.minute.toLong())
        ?.minusSeconds(start.second.toLong())
}