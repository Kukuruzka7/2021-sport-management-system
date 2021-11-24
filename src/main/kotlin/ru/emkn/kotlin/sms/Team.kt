package ru.emkn.kotlin.sms

class TeamName(val name: String) {
    override fun toString() = name
}

class Team(val teamName: TeamName, var athletes: List<Athlete>)