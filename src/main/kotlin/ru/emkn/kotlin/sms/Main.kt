package ru.emkn.kotlin.sms

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import ru.emkn.kotlin.sms.SportType.Companion.getSportTypeFromString
import ru.emkn.kotlin.sms.application.Application
import java.io.File


val INPUT_DATA_FORMAT =
    listOf("name", "sportType", "date", "fileNameOfApplication")

enum class Fields {
    NAME, SPORT_TYPE, DATE, FILE_NAME_OF_APPLICATION
}

fun main(args: Array<String>) {
    //пока написала для случая, когда нам выдают название соревнования, спорт, дату и один файл с названиями файлов в аргументы командной строки
    println("Введите: название соревнования, спорт, дату и файл с названиями от команд.") //это надо исправить если поменяем формат ввода данных, лень пока переписывать
    if (args.size != Fields.values().size) {
        println("Вы ввели, что-то не то. Попробуйте еще раз.")
        return
    }
    val name = args[Fields.NAME.ordinal]
    val sportType: SportType = getSportTypeFromString(args[Fields.SPORT_TYPE.ordinal])
    val date: LocalDate = args[Fields.DATE.ordinal].toLocalDate()
    val fileName = args[Fields.FILE_NAME_OF_APPLICATION.ordinal]
    if (sportType == SportType.ERR) {
        println("Спорт ${args[Fields.SPORT_TYPE.ordinal]} наша система не поддерживает.")
        return
    }
    //TODO("проверка на дату")
    val application: Application
    try {
        val teamApplicationNames = File(fileName).readLines()
        application = Application(teamApplicationNames.map {
            File(it)
        })
    } catch (e: Exception) {
        println(e.message)
        return
    }
    Competition(MetaInfo(name, date, sportType), application)
}
