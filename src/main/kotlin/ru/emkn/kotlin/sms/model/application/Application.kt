package ru.emkn.kotlin.sms.model.application

import java.io.File

class Application(csvList: List<File>) {
    //создание списка заявок команд
    val teamApplicationsList: List<TeamApplication> = List(csvList.size) { TeamApplication(csvList[it], it) }
}