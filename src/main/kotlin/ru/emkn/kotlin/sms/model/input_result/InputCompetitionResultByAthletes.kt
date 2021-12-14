package ru.emkn.kotlin.sms.model.input_result

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.model.result_data.Table
import java.io.File

class InputCompetitionResultByAthletes(override val fileName: String) : InputCompetitionResult() {
    //чтение из файла
    private val rows = csvReader().readAll(File(fileName).readText())

    //карта, которая каждому атлету сопоставляет его результаты на чекпоинтах
    private val resultsOnPoints = rows.map { InputAthleteResults(it) }
    override fun toTable(): Table = Table(resultsOnPoints.associateBy({ it.number }, { it.resultsOnCheckPoints }))

}