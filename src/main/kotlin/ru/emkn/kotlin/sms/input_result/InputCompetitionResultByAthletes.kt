package ru.emkn.kotlin.sms.input_result

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.result_data.Table
import java.io.File

class InputCompetitionResultByAthletes(override val fileName: String) : InputCompetitionResult() {
    private val rows = csvReader().readAll(File(fileName).readText())
    private val resultsOnPoints = rows.map { InputAthleteResults(it) }
    override fun toTable(): Table = Table(resultsOnPoints.associateBy({ it.number }, { it.resultsOnCheckPoints }))

}