package ru.emkn.kotlin.sms.finishprotocol

import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.github.doyaaaaaken.kotlincsv.client.ICsvFileWriter
import ru.emkn.kotlin.sms.*
import java.io.File
import java.time.LocalDateTime

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

    //Скачиваем нужные данные из competition
    private val athletes = competition.athleteList
    private val groups = competition.groupList
    private val teams = competition.teamList

    companion object {
        const val dir = "src/main/resources/competitions/"
    }

    private val path = """$dir${competition.info.name}/finishProtocol/"""

    init {
        createDir(path)
    }

    private val athleteResult: List<AthleteResult> =
        athletes.map { makeIndividualResults(it) }

    private fun makeIndividualResults(athlete: Athlete): AthleteResult {
        //Время начала и конца путешествия одного чела
        val startTime = table.startTime[athlete.number]
        val finishTime = table[athlete.number]?.get(FinishCheckPoint)?.date
        //Очень сильно просим, чтобы чел начал дистанцию
        require(startTime != null) { "Нет стартового времени у чела под номером ${athlete.number}" }
        return AthleteResult(athlete, finishTime - startTime)
    }

    //Делает общие списки групп
    private val groupProtocols: List<GroupProtocol> =
        groups.map { GroupProtocol(it, makeSortedResultsInGroup(it, athleteResult)) }

    //Выставляет номера спортсменов в их группе
    private fun makeSortedResultsInGroup(group: Group, athleteResult: List<AthleteResult>): List<AthleteProtocol> {
        val sortedList = athleteResult.filter { group.athletes.contains(it.athlete) }.sortedBy { it.finishTime }
        return sortedList.map { AthleteProtocol(it.athlete, it.finishTime, sortedList.indexOf(it) + 1) }
    }

    //Объединяет списки по группам в общий список
    private val athleteProtocols: List<AthleteProtocol> =
        groupProtocols.fold(emptyList()) { initial, it -> initial + it.protocol }

    //Разделяет общий список на списки по командам
    private val teamProtocols: List<TeamProtocol> =
        teams.map { TeamProtocol(it, athleteProtocols) }

    val csvByTeams = generateCSVbyTeams()
    val csvByGroups = generateCSVbyGroups()
    val overallCSV = generateOverallCSV()

    private fun generateCSVbyGroups() {
        val dirName = path + "groups/"
        File(dirName).delete()
        createDir(dirName)
        groupProtocols.forEach { it.toCSV(dirName) }
    }

    private fun generateOverallCSV() {
        val fileName = path + "overallCSV"
        File(fileName).delete()
        File(fileName).createNewFile()
        CsvWriter().open(fileName) {
            writeRow("Общий протокол")
            athleteProtocols.sortedBy { it.athlete.groupName.groupName }.forEach {
                writeAthleteProtocol(it)
            }
        }
    }


    private fun generateCSVbyTeams() {
        val dirName = path + "teams/"
        File(dirName).delete()
        createDir(dirName)
        teamProtocols.forEach { it.toCSV(dirName) }
    }

}

//Функция разности двух LocalDateTime
operator fun LocalDateTime?.minus(start: LocalDateTime): LocalDateTime? {
    return this?.minusHours(start.hour.toLong())?.minusMinutes(start.minute.toLong())
        ?.minusSeconds(start.second.toLong())
}

fun ICsvFileWriter.writeAthleteProtocol(it: AthleteProtocol) {
    writeRow(
        it.athlete.groupName.toString(),
        it.place,
        it.athlete.number.value,
        it.athlete.name.lastName,
        it.athlete.name.firstName,
        it.athlete.birthDate.year,
        it.athlete.sportCategory.toString(),
        it.finishTime.toString(),
    )
}

fun createDir(path: String) {
    if (!File(path).exists()) {
        try {
            File(path).mkdir()
        } catch (_: Exception) {
            throw DirectoryCouldNotBeCreated(path)
        }
    }
}