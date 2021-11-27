package ru.emkn.kotlin.sms

import ru.emkn.kotlin.sms.result_data.ResultData

//Абстракция, промежуточный шаг между csv-файлом и Table
abstract class InputCompetitionResult {
    abstract fun toTable(): Table
}

