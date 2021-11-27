package ru.emkn.kotlin.sms.finishprotocol

import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.github.doyaaaaaken.kotlincsv.client.ICsvFileWriter
import kotlinx.datetime.*
import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.athlete.Athlete
import ru.emkn.kotlin.sms.result_data.ResultData
import java.io.File

data class AthleteResult(
    val athlete: Athlete,
    val finishTime: LocalDateTime?,
)

data class AthleteProtocol(
    val athlete: Athlete,
    val finishTime: LocalDateTime?,
    val num: Int,
    val lag: LocalDateTime?,
) {
    val place = if (finishTime != null) num else null
}

class FinishProtocol(private val data: ResultData, competition: Competition) {

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
        generateCSVbyGroups()
        generateOverallCSV()
        generateCSVbyTeams()
    }

    private val athleteResult: List<AthleteResult> =
        athletes.map { makeIndividualResults(it) }

    private fun makeIndividualResults(athlete: Athlete): AthleteResult {
        //Время начала и конца путешествия одного чела
        val startTime = data.startTime[athlete.number]
        val finishTime = data.table[athlete.number]?.last()?.date
        //Очень сильно просим, чтобы чел начал дистанцию
        require(startTime != null) { "Нет стартового времени у чела под номером ${athlete.number}" }
        return AthleteResult(athlete, finishTime - startTime)
    }

    //Делает общие списки групп
    private val groupProtocols: List<GroupProtocol> =
        groups.map { GroupProtocol(it, makeSortedResultsInGroup(it, athleteResult)) }

    //Выставляет номера спортсменов в их группе
    private fun makeSortedResultsInGroup(group: Group, athleteResult: List<AthleteResult>): List<AthleteProtocol> {
        val listWithoutDisqualified =
            athleteResult.filter { group.athletes.contains(it.athlete) && it.finishTime != null }
        val listDisqualified = athleteResult.filter { group.athletes.contains(it.athlete) && it.finishTime == null }
        val sortedList = listWithoutDisqualified.sortedBy { it.finishTime } + listDisqualified
        val bestTime = listWithoutDisqualified.first().finishTime ?: LocalDateTime(2021, 12, 1, 0, 0, 0)
        return sortedList.map {
            AthleteProtocol(
                it.athlete,
                it.finishTime,
                sortedList.indexOf(it) + 1,
                it.finishTime - bestTime
            )
        }
    }

    //Объединяет списки по группам в общий список
    private val athleteProtocols: List<AthleteProtocol> =
        groupProtocols.fold(emptyList()) { initial, it -> initial + it.protocol }

    //Разделяет общий список на списки по командам
    private val teamProtocols: List<TeamProtocol> =
        teams.map { TeamProtocol(it, athleteProtocols) }

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
            groupProtocols.forEach { groupProtocol ->
                writeRow(groupProtocol.group.race.groupName)
                writeInfoRow()
                groupProtocol.protocol.forEach { writeAthleteProtocol(it) }
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
    if (this == null) return null
    return LocalDateTime(
        2021,
        12,
        1,
        hour = this.hour - start.hour,
        minute = this.minute - start.minute,
        second = this.second - start.second
    )
}

fun ICsvFileWriter.writeInfoRow() {
    writeRow(
        "№ п/п",
        "Номер",
        "Фамилия",
        "Имя",
        "Г.р.",
        "Разр.",
        "Команда",
        "Результат",
        "Место",
        "Отставание"
    )
}

fun ICsvFileWriter.writeAthleteProtocol(it: AthleteProtocol) {
    writeRow(
        it.num,
        it.athlete.number.value,
        it.athlete.name.lastName,
        it.athlete.name.firstName,
        it.athlete.birthDate.year,
        it.athlete.sportCategory.toString(),
        it.athlete.teamName.toString(),
        it.finishTime?.toString() ?: "снят",
        it.place,
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