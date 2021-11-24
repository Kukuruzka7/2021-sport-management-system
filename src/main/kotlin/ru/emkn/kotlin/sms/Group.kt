package ru.emkn.kotlin.sms


class Race(val groupName: String) {
    val distance: Int = TODO()
    val numberOfCheckPoints: Int = TODO()
    override fun toString() = groupName
}

class Group(val race: Race, var athletes: List<Athlete>)

