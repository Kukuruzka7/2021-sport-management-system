package ru.emkn.kotlin.sms

class GroupName(val groupName: String) {
    override fun toString() = groupName
}

class Race(val groupName: GroupName) {
    val distance: Int = TODO()
    val numberOfCheckPoints: Int = TODO()
    override fun toString() = groupName.toString()
}

class Group(val race: Race, var athletes: List<Athlete>)

