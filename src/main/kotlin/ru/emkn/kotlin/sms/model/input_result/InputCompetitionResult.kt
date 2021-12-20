package ru.emkn.kotlin.sms.model.input_result

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.InvalidCSV
import ru.emkn.kotlin.sms.model.result_data.Table
import java.io.File

//Абстракция, промежуточный шаг между csv-файлом и Table
abstract class InputCompetitionResult {
    abstract val rows: List<List<String>>
    abstract fun toTable(): Table

    protected companion object {
        fun readText(fileName: String): List<List<String>> {
            return try {
                csvReader().readAll(File(fileName).readText())
            } catch (_: Exception) {
                throw InvalidCSV(fileName)
            }
        }
    }
}

