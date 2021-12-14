package ru.emkn.kotlin.sms.model

import ru.emkn.kotlin.sms.model.athlete.Athlete

class TeamName(val name: String) {
    override fun toString() = name
}

class Team(val teamName: TeamName, var athletes: List<Athlete>)