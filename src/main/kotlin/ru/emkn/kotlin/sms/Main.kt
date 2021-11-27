package ru.emkn.kotlin.sms

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import ru.emkn.kotlin.sms.SportType.Companion.getSportTypeFromString
import ru.emkn.kotlin.sms.UserBehavior.Companion.getBehavior
import ru.emkn.kotlin.sms.application.Application
import ru.emkn.kotlin.sms.startprotocol.StartProtocol
import java.io.File

enum class FieldsStart {
    NAME, SPORT_TYPE, DATE, FILE_NAME_OF_APPLICATION
}

enum class FieldsFinish {
    PATH_TO_COMPETITION_DATA
}

enum class UserBehavior(val behavior: String) {
    START("старт"), FINISH("финал"), ERR("");

    companion object {
        fun getBehavior(behavior: String): UserBehavior {
            for (value in values()) {
                if (value.behavior == behavior.lowercase()) {
                    return value
                }
            }
            return ERR
        }
    }
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Вы ничего не ввели. Попробуйте еще раз.")
        return
    }
    when (getBehavior(args[0])) {
        UserBehavior.START -> start(args.slice(1..args.lastIndex))
        UserBehavior.FINISH -> finish(args.slice(1..args.lastIndex))
        UserBehavior.ERR -> println("Вы ввели не корректную команду. Попробуйте еще раз.")
    }
}

fun start(inputData: List<String>) {
    if (inputData.size != FieldsStart.values().size) {
        println("Вы ввели что-то не то. Попробуйте еще раз.")
        return
    }
    val name = inputData[FieldsStart.NAME.ordinal]
    val sportType: SportType = getSportTypeFromString(inputData[FieldsStart.SPORT_TYPE.ordinal])
    val dateString = inputData[FieldsStart.DATE.ordinal]
    val fileName = inputData[FieldsStart.FILE_NAME_OF_APPLICATION.ordinal]
    if (sportType == SportType.ERR) {
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
    val competition = Competition(MetaInfo(name, date, sportType), application)
    //возможно дальше что-то не то
    competition.toCompetitionData().save("TODO()")
    val startProtocol = StartProtocol(competition.groupList, competition.info.name)
    println("Стартовые протоколы для соревнования ${competition.info.name} сохранены в папке resources.")
    //видимо еще хотим вернуть CompetitionData.
}

fun finish(slice: List<String>) {

}