package ru.emkn.kotlin.sms

class GroupType(val name: String) {
}

class Race(val groupName: GroupType) {
//    val distance: Int = TODO()
//    val numberOfCheckPoints: Int = TODO()
}

//зачем передавать type, если можно сразу передавать Race(type)
class Group(val name: GroupType, val athletes: List<Athlete>) {
    val race: Race = Race(name)
}

