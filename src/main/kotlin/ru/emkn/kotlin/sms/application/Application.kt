package ru.emkn.kotlin.sms.application

import ru.emkn.kotlin.sms.SportType
import ru.emkn.kotlin.sms.TeamApplication
import java.io.File

class Application(csvList: List<File>, sportType: SportType) {
    val teamApplicationsList: List<TeamApplication> = List(csvList.size) { TeamApplication(csvList[it], it, sportType) }
}