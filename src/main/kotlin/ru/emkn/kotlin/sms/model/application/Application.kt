package ru.emkn.kotlin.sms.model.application

import java.io.File

class Application(val teamApplications: List<TeamApplication>) {

    //создание списка заявок команд
    companion object {
        fun create(csvList: List<File>): Application =
            Application(List(csvList.size) { TeamApplication(csvList[it], it) })
    }
}