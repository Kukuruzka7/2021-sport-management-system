package ru.emkn.kotlin.sms.model.input_result

import ru.emkn.kotlin.sms.model.result_data.Table
import ru.emkn.kotlin.sms.readCSV

class InputCompetitionResultByAthletes(override val rows: List<List<String>>) : InputCompetitionResult() {

    //чтение из файла
    constructor(fileName: String) : this(readCSV(fileName))

    //карта, которая каждому атлету сопоставляет его результаты на чекпоинтах
    private val resultsOnPoints = rows.map { InputAthleteResults(it) }
    override fun toTable(): Table = Table(resultsOnPoints.associateBy({ it.number }, { it.resultsOnCheckPoints }))
}