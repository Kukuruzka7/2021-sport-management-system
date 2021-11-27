package ru.emkn.kotlin.sms.input_result

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.InputCompetitionResult
import ru.emkn.kotlin.sms.result_data.Table
import java.io.File


//
class InputCompetitionResultByAthletes(override val fileName: String) : InputCompetitionResult() {
    val rows = csvReader().readAll(File(fileName).readText())
    val resultsOnPoints = rows.map { InputAthleteResults(it) }
    override fun toTable() = Table(resultsOnPoints.associateBy({ it.number }, { it.resultsOnCheckPoints }))

}