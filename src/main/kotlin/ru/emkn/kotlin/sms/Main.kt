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

var sport = SportType.ERROR

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
    if (inputData.size != FieldsStart.values().size) {
        println("Вы ввели что-то не то. Попробуйте еще раз.")
        return
    }
    val name = inputData[FieldsStart.NAME.ordinal]
    sport = getSportType(inputData[FieldsStart.SPORT_TYPE.ordinal])
    val dateString = inputData[FieldsStart.DATE.ordinal]
    val fileName = inputData[FieldsStart.FILE_NAME_OF_APPLICATION.ordinal]
    if (sport == SportType.ERROR) {
        println("Спорт ${inputData[FieldsStart.SPORT_TYPE.ordinal]} наша система не поддерживает.")
        return
    }
    val date: LocalDate
    try {
        date = dateString.toLocalDate()
    } catch (e: Exception) {
        println("Дата состязания $dateString не корректна.")
        return
    }
    val application: Application
    val teamApplicationNames: List<String>
    try {
        teamApplicationNames = File(fileName).readLines()
    } catch (e: Exception) {
        println("Файл $fileName не может быть прочитан.")
        return
    }
    try {
        application = Application(teamApplicationNames.map {
            File(it)
        })
    } catch (e: Exception) {
        println(e.message)
        return
    }
    val competition = Competition(MetaInfo(name, date, sport), application)
    StartProtocol(competition.groupList, dir + competition.info.name + "/")
    competition.toCompetitionData().save(dir + competition.info.name + "/competitionData.csv")
    println("Стартовые протоколы для соревнования ${competition.info.name} сохранены в src/main/resources/competitions/${competition.info.name}/startProtocol/.")
}

fun finishByAthletes(inputData: Array<String>) {
    if (inputData.size != FieldsFinish.values().size) {
        println("Вы ввели что-то не то. Попробуйте еще раз.")
        return
    }
    val fileName = inputData[FieldsFinish.PATH_TO_FILE.ordinal]
    val name = inputData[FieldsFinish.NAME.ordinal]
    val data: List<List<String>>
    val competition: Competition
    if (!File(dir + name + "/competitionData.csv").exists()) {
        println("Такого соревнования еще не проводилось. Сначала введите заявки на участие.")
        return
    }
        data = csvReader().readAll(File(dir + name + "/competitionData.csv"))
        competition = Competition(CompetitionData(data))
    
    val athletesResults: ResultData
    try {
        athletesResults =
            ResultData(InputCompetitionResultByAthletes(fileName).toTable(), CompetitionData(data).getStartTime())
    } catch (e: Exception) {
        println(e.message)
        return
    }
    FinishProtocol(athletesResults, competition)
    println("Финальные протоколы для соревнования ${competition.info.name} сохранены в src/main/resources/competitions/${competition.info.name}/finishProtocol/.")
}

fun finishByCheckPoints(inputData: Array<String>) {
    if (inputData.size != FieldsFinish.values().size) {
        println("Вы ввели что-то не то. Попробуйте еще раз.")
        return
    }
    val fileName = inputData[FieldsFinish.PATH_TO_FILE.ordinal]
    val name = inputData[FieldsStart.NAME.ordinal]
    val data: List<List<String>>
    val competition: Competition
    if (!File(dir + name + "/competitionData").exists()) {
        println("Такого соревнования еще не проводилось. Сначала введите заявки на участие.")
        return
    }
    try {
        data = csvReader().readAll(File(dir + name + "/competitionData.csv"))
        competition = Competition(CompetitionData(data))
    } catch (e: Exception) {
        println("Произошла ошибка, попробуйте заново загрузить заявки.")
        return
    }
    val athletesResults: ResultData
    try {
        athletesResults = ResultData(
            InputCompetitionResultByCheckPoints(fileName, competition).toTable(),
            CompetitionData(data).getStartTime()
        )
    } catch (e: Exception) {
        println(e.message)
        return
    }
    FinishProtocol(athletesResults, competition)
    println("Финальные протоколы для соревнования ${competition.info.name} сохранены в src/main/resources/competitions/${competition.info.name}/finishProtocol/.")
}