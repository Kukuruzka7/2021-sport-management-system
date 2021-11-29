package ru.emkn.kotlin.sms.input_result

import ru.emkn.kotlin.sms.result_data.Table

//Абстракция, промежуточный шаг между csv-файлом и Table
abstract class InputCompetitionResult {
    abstract val fileName: String
    abstract fun toTable(): Table
}

