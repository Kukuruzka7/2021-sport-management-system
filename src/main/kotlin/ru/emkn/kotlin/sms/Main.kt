package ru.emkn.kotlin.sms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.*
import androidx.compose.ui.window.rememberWindowState

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import logger
import ru.emkn.kotlin.sms.SportType.Companion.getSportType
import ru.emkn.kotlin.sms.UserBehavior.Companion.getBehavior
import ru.emkn.kotlin.sms.application.Application
import ru.emkn.kotlin.sms.finishprotocol.FinishProtocol
import ru.emkn.kotlin.sms.input_result.InputCompetitionResultByAthletes
import ru.emkn.kotlin.sms.input_result.InputCompetitionResultByCheckPoints
import ru.emkn.kotlin.sms.result_data.ResultData
import ru.emkn.kotlin.sms.startprotocol.StartProtocol
import java.io.File

enum class FieldsStart {
    BEHAVIOR, NAME, SPORT_TYPE, DATE, FILE_NAME_OF_APPLICATION
}

enum class FieldsFinish {
    BEHAVIOR, PATH_TO_FILE, NAME
}

enum class UserBehavior(val behavior: String) {
    START("start"), FINISH_BY_ATHLETES("finish_by_athletes"), FINISH_BY_CHECKPOINTS("finish_by_checkpoints"), ERROR("");

    companion object {
        fun getBehavior(behavior: String): UserBehavior {
            for (value in values()) {
                if (value.behavior == behavior.lowercase()) {
                    return value
                }
            }
            return ERROR
        }
    }
}

const val dir = "src/main/resources/competitions/"

fun main(args: Array<String>) {
    logger.info { "Начало работы программы." }

    if (checkEmptyInput(args)) return
    when (getBehavior(args[0])) {
        UserBehavior.START -> start(args)
        UserBehavior.FINISH_BY_ATHLETES -> finishByAthletes(args)
        UserBehavior.FINISH_BY_CHECKPOINTS -> finishByCheckPoints(args)
        UserBehavior.ERROR -> println("Вы ввели не корректную команду. Попробуйте еще раз.")
    }
    logger.info { "Завершение работы программы." }
}

private fun checkEmptyInput(args: Array<String>): Boolean {
    if (args.isEmpty()) {
        println("Вы ничего не ввели. Попробуйте еще раз.")
        return true
    }
    return false
}

fun start(inputData: Array<String>) {
    logger.info { "Вызов функции start. Обработка входных данных." }
    if (checkForEmptyInputStart(inputData)) return
    val name = inputData[FieldsStart.NAME.ordinal]
    val sport = getSportType(inputData[FieldsStart.SPORT_TYPE.ordinal])
    if (checkSportType(inputData)) return
    val dateString = inputData[FieldsStart.DATE.ordinal]
    val fileName = inputData[FieldsStart.FILE_NAME_OF_APPLICATION.ordinal]
    val date: LocalDate = getDate(dateString) ?: return
    logger.info { "Обработка путей к заявкам." }
    val teamApplicationNames: List<String> = getTeamApplicationNames(fileName) ?: return
    logger.info { "Создание Application." }
    val application: Application = getApplication(teamApplicationNames) ?: return
    logger.info { "Application создан." }
    logger.info { "Создание Competition." }
    val competition = Competition(MetaInfo(name, date, sport), application)
    logger.info { "Competition создан." }
    logger.info { "Создание StartProtocol." }
    try {
        StartProtocol(competition.groupList, dir + competition.info.name + "/")
    } catch (e: Exception) {
        println(e.message)
        return
    }
    logger.info { "StartProtocol создан." }
    logger.info { "Сохранение Competition" }
    competition.toCompetitionData().save(
        dir + competition.info.name + "/competitionData.csv",
        dir + competition.info.name + "/competitionInfo.csv"
    )
    logger.info { "Competition успешно сохранен." }
    println("Стартовые протоколы для соревнования ${competition.info.name} сохранены в src/main/resources/competitions/${competition.info.name}/startProtocol/.")
    logger.info { "Завершение start." }
}

fun finishByAthletes(inputData: Array<String>) {
    logger.info { "Вызов функции finishByAthletes. Обработка входных данных." }
    if (checkForEmptyInputFinish(inputData)) return
    val fileName = inputData[FieldsFinish.PATH_TO_FILE.ordinal]
    val name = inputData[FieldsFinish.NAME.ordinal]
    logger.info { "Проверка на существование соревнования." }
    if (checkCompetitionExist(name)) return
    logger.info { "Получение результатов атлетов." }
    val data: List<List<String>> = getData(name) ?: return
    logger.info { "Результаты атлетов получены." }
    val info: MetaInfo = getMetaInfo(name) ?: return
    logger.info { "Создание Competition." }
    val competition: Competition = getCompetition(data, info) ?: return
    logger.info { "Competition создан." }
    val athletesResults: ResultData = resultDataByAthlete(fileName, data, info) ?: return
    logger.info { "Создание FinishProtocol." }
    try {
        FinishProtocol(athletesResults, competition)
    } catch (e: Exception) {
        println(e.message)
        return
    }
    logger.info { "FinishProtocol успешно сохранен." }
    println("Финальные протоколы для соревнования ${competition.info.name} сохранены в src/main/resources/competitions/${competition.info.name}/finishProtocol/.")
    logger.info { "Завершение finishByAthletes" }
}

fun finishByCheckPoints(inputData: Array<String>) {
    logger.info { "Вызов функции finishByCheckPoints. Обработка входных данных." }
    if (checkForEmptyInputFinish(inputData)) return
    val fileName = inputData[FieldsFinish.PATH_TO_FILE.ordinal]
    val name = inputData[FieldsFinish.NAME.ordinal]
    logger.info { "Проверка на существование соревнования." }
    if (checkCompetitionExist(name)) return
    logger.info { "Получение результатов по контрольным точкам." }
    val data: List<List<String>> = getData(name) ?: return
    logger.info { "Результаты получены." }
    val info: MetaInfo = getMetaInfo(name) ?: return
    logger.info { "Создание Competition." }
    val competition: Competition = getCompetition(data, info) ?: return
    logger.info { "Competition создан." }
    val athletesResults: ResultData = resultDataByCheckPoints(fileName, data, competition, info) ?: return
    logger.info { "Создание FinishProtocol." }
    try {
        FinishProtocol(athletesResults, competition)
    } catch (e: Exception) {
        println(e.message)
        return
    }
    logger.info { "FinishProtocol успешно сохранен." }
    println("Финальные протоколы для соревнования ${competition.info.name} сохранены в src/main/resources/competitions/${competition.info.name}/finishProtocol/.")
    logger.info { "Завершение finishByCheckPoints." }
}

private fun getApplication(teamApplicationNames: List<String>): Application? {
    try {
        return Application(teamApplicationNames.map {
            File(it)
        })
    } catch (e: Exception) {
        println(e.message)
        return null
    }
}

private fun getTeamApplicationNames(fileName: String): List<String>? {
    try {
        return File(fileName).readLines()
    } catch (e: Exception) {
        println("Файл $fileName не может быть прочитан.")
        return null
    }
}

private fun getDate(dateString: String): LocalDate? {
    try {
        return dateString.toLocalDate()
    } catch (e: Exception) {
        println("Дата состязания $dateString не корректна.")
        return null
    }
}

private fun checkSportType(inputData: Array<String>): Boolean {
    if (getSportType(inputData[FieldsStart.SPORT_TYPE.ordinal]) == SportType.X) {
        println("Спорт ${inputData[FieldsStart.SPORT_TYPE.ordinal]} наша система не поддерживает.")
        return true
    }
    return false
}

private fun checkForEmptyInputStart(inputData: Array<String>): Boolean {
    if (inputData.size != FieldsStart.values().size) {
        println("Вы ввели что-то не то. Попробуйте еще раз.")
        return true
    }
    return false
}

private fun checkForEmptyInputFinish(inputData: Array<String>): Boolean {
    if (inputData.size != FieldsFinish.values().size) {
        println("Вы ввели что-то не то. Попробуйте еще раз.")
        return true
    }
    return false
}

private fun resultDataByAthlete(fileName: String, data: List<List<String>>, info: MetaInfo): ResultData? {
    try {
        return ResultData(
            InputCompetitionResultByAthletes(fileName).toTable(),
            CompetitionData(data, info.toStringList()).getStartTime()
        )
    } catch (e: Exception) {
        println(e.message)
        return null
    }
}

private fun resultDataByCheckPoints(
    fileName: String,
    data: List<List<String>>,
    competition: Competition,
    info: MetaInfo
): ResultData? {
    try {
        return ResultData(
            InputCompetitionResultByCheckPoints(fileName, competition).toTable(),
            CompetitionData(data, info.toStringList()).getStartTime()
        )
    } catch (e: Exception) {
        println(e.message)
        return null
    }
}

private fun getCompetition(data: List<List<String>>, info: MetaInfo): Competition? {
    try {
        return Competition(CompetitionData(data, info.toStringList()))
    } catch (e: Exception) {
        println(e.message)
        return null
    }
}

private fun getData(name: String): List<List<String>>? {
    try {
        return csvReader().readAll(File("$dir$name/competitionData.csv"))
    } catch (e: Exception) {
        println("Что-то пошло не так, попробуйте ввести заявки заново.")
        return null
    }
}

private fun getMetaInfo(name: String): MetaInfo? = try {
    MetaInfo(csvReader().readAll(File("$dir$name/competitionInfo.csv"))[0])
} catch (e: Exception) {
    println("Что-то пошло не так, попробуйте ввести заявки заново.")
    null
}

private fun checkCompetitionExist(name: String): Boolean {
    if (!File("$dir$name/competitionData.csv").exists()) {
        println("Такого соревнования еще не проводилось. Сначала введите заявки на участие.")
        return true
    }
    return false
}