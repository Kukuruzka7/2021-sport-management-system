package ru.emkn.kotlin.sms.model.input_result

import ru.emkn.kotlin.sms.model.result_data.Table

//Абстракция, промежуточный шаг между csv-файлом и Table
abstract class InputCompetitionResult {
    abstract val fileName: String
    abstract val rows: List<List<String>>
    abstract fun toTable(): Table
}

