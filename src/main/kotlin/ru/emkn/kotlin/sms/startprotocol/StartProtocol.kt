package ru.emkn.kotlin.sms.startprotocol

import ru.emkn.kotlin.sms.Group
import ru.emkn.kotlin.sms.finishprotocol.createDir

class StartProtocol(groups : List<Group>, competitionName: String) {
    private val generateCSV: List<GroupStartProtocol>
    private val path: String

    companion object {
        const val dir = "src/main/resources/competitions/"
    }

    init {
        path = "$dir$competitionName/startProtocol/"
        createDir(path)
        generateCSV = groups.map { GroupStartProtocol(it, path) }
        generateCSV.forEach { it.toCSV }
    }
}