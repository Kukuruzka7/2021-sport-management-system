package ru.emkn.kotlin.sms

interface InputCompetitionResult {
    fun toTable(): Table
}

class InputCompetitionResultByAthletes(val result: List<InputAthleteResults>) : InputCompetitionResult {
    override fun toTable(): Table {
        TODO("Not yet implemented")
    }
}

class InputAthleteResults {

}

class InputCompetitionResultByCheckPoints(val result: List<InputCheckpointResults>) : InputCompetitionResult {

    override fun toTable(): Table {
        TODO("Not yet implemented")
    }
}

class InputCheckpointResults {

}
