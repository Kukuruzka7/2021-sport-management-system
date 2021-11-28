package ru.emkn.kotlin.sms.startprotocol

import ru.emkn.kotlin.sms.Group
import ru.emkn.kotlin.sms.finishprotocol.createDir

class StartProtocol(groups: List<Group>, competitionPath: String) {
    private val generateCSV: List<GroupStartProtocol>
    private val path = "${competitionPath}startProtocol/"

    init {
        createDir(competitionPath)
        createDir(path)
        generateCSV = groups.map { GroupStartProtocol(it, path) }
        generateCSV.forEach { it.toCSV }
    }
}