package ru.emkn.kotlin.sms

class StartProtocol(listGroup: List<Group>) {
    val generateCsv: List<GroupStartProtocol>
    init {
        generateCsv = listGroup.map { GroupStartProtocol(it) }
    }
}