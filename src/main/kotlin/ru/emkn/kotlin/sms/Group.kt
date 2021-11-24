package ru.emkn.kotlin.sms

class GroupName(val groupName: String)

class Race(val groupName: GroupName) {
    val distance: Int = TODO()
    val numberOfCheckPoints: Int = TODO()
}

class Group(val race: Race, val athletes: List<Athlete>) {

}

