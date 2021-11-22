package ru.emkn.kotlin.sms

class TeamProtocol() {
    fun toCSV(): Any = TODO()
}

class GroupProtocol {
    fun toCSV(): Any = TODO()
}

class AthleteProtocol

class Protocol(table: Table) {
    private val groupProtocol: Map<Group, GroupProtocol> = TODO()
    private val teamProtocols: Map<Team, TeamProtocol> = TODO()
    private val athleteProtocol: Map<Athlete, AthleteProtocol> = TODO()

    val csvByTeams = generateCSVbyTeams()
    val csvByGroups = generateCSVbyGroups()
    val overallCSB = generateOverallCSV()

    private fun generateCSVbyTeams(): Any = TODO()
    private fun generateCSVbyGroups(): Any = TODO()
    private fun generateOverallCSV(): Any = TODO()

}