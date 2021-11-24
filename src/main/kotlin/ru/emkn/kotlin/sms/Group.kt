package ru.emkn.kotlin.sms

class GroupName(val groupName: String)

class Race(val groupName: GroupName) {
    val distance: Int = TODO()
    val numberOfCheckPoints: Int = TODO()
    override fun toString() = groupName
}

class Group(val race: Race, var athletes: List<Athlete>)

