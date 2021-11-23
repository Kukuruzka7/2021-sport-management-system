package ru.emkn.kotlin.sms

import java.io.File

class Application(csvList: List<File>) {
    val teamApplicationsList: List<TeamApplication> = List(csvList.size) { TeamApplication(csvList[it],it) }
}