package ru.emkn.kotlin.sms

import java.util.Date

enum class SportType {
    RUNNING
}

data class MetaInfo(val name: String, val date: Date, val sport: SportType)

class Competition(val info: MetaInfo, val application: Application) {
    val teams: List<Team> = TODO()
    val athletes: List<Athlete> = TODO()
    val groups: List<Group> = TODO()
    val numerator: Map<Athlete, AthleteNumber> = TODO()

    val protocol = StartProtocol(groups)

    init {
     ///   protocol.save(TODO())
    }

    fun run(table: Table): FinishProtocol {
        TODO()
    }
}