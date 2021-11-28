package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
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

var sport = SportType.X

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Вы ничего не ввели. Попробуйте еще раз.")
        return
    }
    when (getBehavior(args[0])) {
        UserBehavior.START -> start(args)
        UserBehavior.FINISH_BY_ATHLETES -> finishByAthletes(args)
        UserBehavior.FINISH_BY_CHECKPOINTS -> finishByCheckPoints(args)
        UserBehavior.ERROR -> println("Вы ввели не корректную команду. Попробуйте еще раз.")
    }
}

fun start(inputData: Array<String>) {
    if (checkForEmptyInput(inputData)) return
    val name = inputData[FieldsStart.NAME.ordinal]
    sport = getSportType(inputData[FieldsStart.SPORT_TYPE.ordinal])
    val dateString = inputData[FieldsStart.DATE.ordinal]
    val fileName = inputData[FieldsStart.FILE_NAME_OF_APPLICATION.ordinal]
    if (checkSportType(inputData)) return
    val date: LocalDate = getDate(dateString) ?: return
    val teamApplicationNames: List<String> = getTeamApplicationNames(fileName) ?: return
    val application: Application = getApplication(teamApplicationNames) ?: return
    val competition = Competition(MetaInfo(name, date, sport), application)
    StartProtocol(competition.groupList, dir + competition.info.name + "/")
    competition.toCompetitionData().save(dir + competition.info.name + "/competitionData.csv", TODO())
    println("Стартовые протоколы для соревнования ${competition.info.name} сохранены в src/main/resources/competitions/${competition.info.name}/startProtocol/.")
}

fun finishByAthletes(inputData: Array<String>) {
    if (checkForEmptyInput(inputData)) return
    val fileName = inputData[FieldsFinish.PATH_TO_FILE.ordinal]
    val name = inputData[FieldsFinish.NAME.ordinal]
    if (checkCompetitionExist(name)) return
    val data: List<List<String>> = getData(name) ?: return
    val competition: Competition = getCompetition(data) ?: return
    val athletesResults: ResultData = resultDataByAthlete(fileName, data) ?: return
    FinishProtocol(athletesResults, competition)
    println("Финальные протоколы для соревнования ${competition.info.name} сохранены в src/main/resources/competitions/${competition.info.name}/finishProtocol/.")
}

fun finishByCheckPoints(inputData: Array<String>) {
    if (checkForEmptyInput(inputData)) return
    val fileName = inputData[FieldsFinish.PATH_TO_FILE.ordinal]
    val name = inputData[FieldsFinish.NAME.ordinal]
    if (checkCompetitionExist(name)) return
    val data: List<List<String>> = getData(name) ?: return
    val competition: Competition = getCompetition(data) ?: return
    val athletesResults: ResultData = resultDataByCheckPoints(fileName, data, competition) ?: return
    FinishProtocol(athletesResults, competition)
    println("Финальные протоколы для соревнования ${competition.info.name} сохранены в src/main/resources/competitions/${competition.info.name}/finishProtocol/.")
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
    if (sport == SportType.X) {
        println("Спорт ${inputData[FieldsStart.SPORT_TYPE.ordinal]} наша система не поддерживает.")
        return true
    }
    return false
}

private fun checkForEmptyInput(inputData: Array<String>): Boolean {
    if (inputData.size != FieldsStart.values().size) {
        println("Вы ввели что-то не то. Попробуйте еще раз.")
        return true
    }
    return false
}

private fun resultDataByAthlete(fileName: String, data: List<List<String>>): ResultData? {
    try {
        //return ResultData(InputCompetitionResultByAthletes(fileName).toTable(), CompetitionData(data).getStartTime(), )
    } catch (e: Exception) {
        println(e.message)
        return null
    }
    return null
}

private fun resultDataByCheckPoints(fileName: String, data: List<List<String>>, competition: Competition): ResultData? {
    try {
        //return ResultData( InputCompetitionResultByCheckPoints(fileName, competition).toTable(), CompetitionData(data).getStartTime() )
    } catch (e: Exception) {
        println(e.message)
        return null
    }
    return null
}

private fun getCompetition(data: List<List<String>>): Competition? {
    try {
        return Competition(CompetitionData(data, TODO()))
    } catch (e: Exception) {
        println(e.message)
        return null
    }
}

private fun getData(name: String): List<List<String>>? {
    try {
        return csvReader().readAll(File(dir + name + "/competitionData.csv"))
    } catch (e: Exception) {
        println("Что-то пошло не так, попробуйте ввести заявки заново.")
        return null
    }
}

private fun checkCompetitionExist(name: String): Boolean {
    if (!File(dir + name + "/competitionData.csv").exists()) {
        println("Такого соревнования еще не проводилось. Сначала введите заявки на участие.")
        return true
    }
    return false
}