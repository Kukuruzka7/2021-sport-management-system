package ru.emkn.kotlin.sms

class StartProtocol(listGroup: List<Group>) {
    val generateCSV: List<GroupStartProtocol>

    init {
        generateCSV = listGroup.map { GroupStartProtocol(it) }
    }
}

class GroupStartProtocol(val group: Group) {
    val toCSV: Any = writeGroupToCSV()

    private fun writeGroupToCSV() {
        TODO("Не обсудили файловую систему")
    }
}