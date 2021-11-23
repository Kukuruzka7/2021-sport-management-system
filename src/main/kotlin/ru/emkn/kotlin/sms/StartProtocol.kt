package ru.emkn.kotlin.sms

import java.io.File

class StartProtocol(listGroup: List<Group>) {
    val generateCsv: List<GroupStartProtocol>
    init {
        generateCsv = listGroup.map { GroupStartProtocol(it) }
    }
}

class GroupStartProtocol(val group: Group) {
    val toCsv: File = TODO()
}