package ru.emkn.kotlin.sms.finishprotocol

import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.github.doyaaaaaken.kotlincsv.client.ICsvFileWriter
import kotlinx.datetime.LocalDateTime
import logger
import ru.emkn.kotlin.sms.Competition
import ru.emkn.kotlin.sms.DirectoryCouldNotBeCreated
import ru.emkn.kotlin.sms.Group
import ru.emkn.kotlin.sms.athlete.Athlete
import ru.emkn.kotlin.sms.result_data.ResultData
import java.io.File
import kotlin.math.max

data class AthleteResult(
    val athlete: Athlete,
    val finishTime: LocalDateTime?,
)

data class AthleteProtocol(
    val athlete: Athlete,
    val finishTime: LocalDateTime?,
    val num: Int,
    val lag: LocalDateTime?,
    val points: Double,
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
        logger.info { "Создан объект класса FinishProtocol" }
        createDir(path)
        generateCSVbyGroups()
        generateOverallCSV()
        generateCSVbyTeams()
        logger.info { "Сгенерированы все возможные итоговые csv протоколы" }
    }

    private val athleteResult: List<AthleteResult> =
        athletes.map { makeIndividualResults(it) }

    //Сделать индивидуальные результаты
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
        logger.info { "Начинаю сортировать CSV по группам" }
        val listWithoutDisqualified =
            athleteResult.filter { group.athletes.contains(it.athlete) && it.finishTime != null }
        val listDisqualified = athleteResult.filter { group.athletes.contains(it.athlete) && it.finishTime == null }
        val sortedList = listWithoutDisqualified.sortedBy { it.finishTime } + listDisqualified
        val bestTime = listWithoutDisqualified.first().finishTime ?: LocalDateTime(2021, 12, 1, 0, 0, 0)
        return sortedList.map {
            val points = calculatePoints(it.finishTime, bestTime)
            AthleteProtocol(
                it.athlete,
                it.finishTime,
                sortedList.indexOf(it) + 1,
                it.finishTime - bestTime,
                points
            )
        }
    }

    //Посчитать очки за забег
    private fun calculatePoints(finishTime: LocalDateTime?, bestTime: LocalDateTime): Double {
        return if (finishTime == null) {
            0.0
        } else max(
            0.0,
            100.0 * (2.0 - 1.0 * finishTime.toInt() / bestTime.toInt())
        )
    }

    //Объединяет списки по группам в общий список
    private val athleteProtocols: List<AthleteProtocol> =
        groupProtocols.fold(emptyList()) { initial, it -> initial + it.protocol }

    //Разделяет общий список на списки по командам
    private val teamProtocols: List<TeamProtocol> =
        teams.map { TeamProtocol(it, athleteProtocols) }

    //Создаёт csv по группам
    private fun generateCSVbyGroups() {
        logger.info { "Начинаю создавать CSV по группам" }
        val dirName = path + "groups/"
        File(dirName).delete()
        createDir(dirName)
        groupProtocols.forEach { it.toCSV(dirName) }
        logger.info { "CSV по группам успешно созданы" }
    }

    //Создаёт общее csv
    private fun generateOverallCSV() {
        logger.info { "Начинаю создавать общее CSV" }
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
        logger.info { "Общее CSV успешно создано" }
    }

    //Создаёт csv по командам
    private fun generateCSVbyTeams() {
        logger.info { "Начинаю создавать CSV по командам" }
        val dirName = path + "teams/"
        File(dirName).delete()
        createDir(dirName)
        teamProtocols.forEach { it.toCSV(dirName) }
        logger.info { "CSV по командам успешно созданы" }
    }
}

//Количество секунд от начала дня
fun LocalDateTime.toInt(): Int = this.hour * 3600 + this.minute * 60 + this.second

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

//Печатает стартовую строчку
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

//Печатает строчку с данными об атлете
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
        it.lag,
    )
}

//Функция создания директории
fun createDir(path: String) {
    logger.info { "Начинаю создавать директорию $path" }
    if (!File(path).isFile) {
        if (File(path).exists()) {
            logger.info { "По пути $path был файл. Его пришлось удалить!" }
            File(path).delete()
        }
        try {
            File(path).mkdir()
        } catch (_: Exception) {
            throw DirectoryCouldNotBeCreated(path)
        }
    }
    logger.info { "Директория $path успешно создана" }
}