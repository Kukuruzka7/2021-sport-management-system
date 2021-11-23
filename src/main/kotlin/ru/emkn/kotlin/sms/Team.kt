package ru.emkn.kotlin.sms

class TeamName(val name: String) {
}

class Team(val teamName: TeamName) {
    var athleteList: List<Athlete> = emptyList()
}