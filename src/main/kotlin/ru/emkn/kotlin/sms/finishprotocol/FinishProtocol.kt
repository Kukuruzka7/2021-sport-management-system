package ru.emkn.kotlin.sms.finishprotocol

import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.github.doyaaaaaken.kotlincsv.client.ICsvFileWriter
import logger
import ru.emkn.kotlin.sms.Competition
import ru.emkn.kotlin.sms.DirectoryCouldNotBeCreated
import ru.emkn.kotlin.sms.Group
import ru.emkn.kotlin.sms.athlete.Athlete
import ru.emkn.kotlin.sms.result_data.ResultData
import ru.emkn.kotlin.sms.startprotocol.toStringWithSeconds
import java.io.File
import java.time.LocalTime
import kotlin.math.max

data class AthleteResult(
    val athlete: Athlete,
    val finishTime: LocalTime?,
)

data class AthleteProtocol(
    val athlete: Athlete,
    val finishTime: LocalTime?,
    val num: Int,
    val lag: LocalTime?,
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

    //Путь к папке с финальными протоколами
    private val path = """$dir${competition.info.name}/finishProtocol/"""


    //Переменная, хранящая список результатов атлетов
    private val athleteResults: List<AthleteResult> =
        athletes.map { makeIndividualResults(it) }

    //Сделать индивидуальные результаты
    private fun makeIndividualResults(athlete: Athlete): AthleteResult {
        logger.trace { "Делаю индивидуальные результаты для ${athlete.number.value}" }
        //Время начала и конца путешествия одного чела
        val startTime = data.startTime[athlete.number]
        val finishTime = data.table[athlete.number]?.last()?.date
        //Очень сильно просим, чтобы чел начал дистанцию
        require(startTime != null) { "Нет стартового времени у чела под номером ${athlete.number}" }
        return AthleteResult(athlete, finishTime - startTime)
    }

    //Делает общие списки групп
    private val groupProtocols: List<GroupProtocol> =
        groups.map { GroupProtocol(it, makeSortedResultsInGroup(it, athleteResults)) }

    //Выставляет номера спортсменов в их группе
    private fun makeSortedResultsInGroup(group: Group, athleteResult: List<AthleteResult>): List<AthleteProtocol> {
        logger.trace { "Начинаю сортировать CSV по группам" }
        val listWithoutDisqualified =
            athleteResult.filter { group.athletes.contains(it.athlete) && it.finishTime != null }
        val listDisqualified = athleteResult.filter { group.athletes.contains(it.athlete) && it.finishTime == null }
        if (listWithoutDisqualified.isEmpty()) {
            return listDisqualified.map {
                AthleteProtocol(
                    it.athlete,
                    it.finishTime,
                    listDisqualified.indexOf(it) + 1,
                    null,
                    0.0
                )
            }
        }
        val sortedList = listWithoutDisqualified.sortedBy { it.finishTime } + listDisqualified
        val bestTime = listWithoutDisqualified.first().finishTime ?: LocalTime.of(0, 0, 0)
        return sortedList.map {
            val points = calculatePoints(it.finishTime, bestTime)
            AthleteProtocol(
                it.athlete,
                it.finishTime,
                sortedList.indexOf(it) + 1,
                if (it.finishTime - bestTime != LocalTime.of(0, 0, 0)) (it.finishTime - bestTime) else null,
                points
            )
        }
    }

    private fun calculatePoints(finishTime: LocalTime?, bestTime: LocalTime): Double {
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
        teams.map { team ->
            TeamProtocol(
                team,
                athleteProtocols.filter { it.athlete.teamName.name == team.teamName.name })
        }

    private fun generateCSVbyGroups() {
        logger.trace { "Начинаю создавать CSV по группам" }
        val dirName = path + "groups/"
        createDir(dirName)
        groupProtocols.forEach { it.toCSV(dirName) }
    }

    private fun generateOverallCSV() {
        logger.trace { "Делаю общий CSV" }
        val fileName = path + "overallCSV.csv"
        if (File(fileName).exists()) {
            File(fileName).delete()
        }
        File(fileName).createNewFile()
        CsvWriter().open(fileName) {
            writeRow("Общий протокол", "", "", "", "", "", "", "", "", "")
            groupProtocols.forEach { groupProtocol ->
                writeRow(groupProtocol.group.race.groupName, "", "", "", "", "", "", "", "", "")
                writeInfoRow()
                groupProtocol.protocol.forEach { writeAthleteProtocol(it) }
            }
        }
    }

    //Генерирует протокол по командам
    private fun generateCSVbyTeams() {
        logger.trace { "Делаю CSV по командам" }
        val dirName = path + "teams/"
        createDir(dirName)
        teamProtocols.forEach { it.toCSV(dirName) }
    }

    init {
        logger.trace { "Создан объект класса FinishProtocol" }
        createDir(path)
        generateCSVbyGroups()
        generateOverallCSV()
        generateCSVbyTeams()
        logger.trace { "Сгенерированы все возможные итоговые csv протоколы" }
    }
}

//Переделывает localTime в количество секунд
fun LocalTime.toInt(): Int = this.hour * 3600 + this.minute * 60 + this.second

//Функция разности двух LocalTime
operator fun LocalTime?.minus(subtrahend: LocalTime): LocalTime? {
    if (this == null) return null
    val thisTime = this.toInt()
    val subtrahendTime = subtrahend.toInt()
    val difference = thisTime - subtrahendTime + 24 * 60 * 60
    return LocalTime.of((difference / 3600) % 24, (difference % 3600) / 60, difference % 60)
}

fun LocalTime.toStringWithoutHours(): String {
    val hours = if (this.hour == 0) "" else "${this.hour}:"
    return "${hours}${this.minute / 10}${this.minute % 10}:${this.second / 10}${this.second % 10}"
}

//Запись информации о таблице
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
        "Отставание",
    )
}

//Записывает строчку по AthleteProtocol
fun ICsvFileWriter.writeAthleteProtocol(it: AthleteProtocol) {
    writeRow(
        it.num,
        it.athlete.number.value,
        it.athlete.name.lastName,
        it.athlete.name.firstName,
        it.athlete.birthDate.year,
        it.athlete.sportCategory.toString(),
        it.athlete.teamName.toString(),
        it.finishTime?.toStringWithSeconds() ?: "снят",
        it.place,
        if (it.lag == null) null else "+" + it.lag.toStringWithoutHours(),
    )
}

//Создать директорию
fun createDir(path: String) {
    logger.trace { "Начинаю создавать директорию $path" }
    if (!File(path).isFile) {
        if (File(path).exists()) {
            logger.trace { "По пути $path был файл. Его пришлось удалить!" }
            File(path).delete()
        }
        try {
            File(path).mkdir()
        } catch (_: Exception) {
            throw DirectoryCouldNotBeCreated(path)
        }
    }
    logger.trace { "Директория $path успешно создана" }
}