package ru.emkn.kotlin.sms

//Абстракция, промежуточный шаг между csv-файлом и Table
abstract class InputCompetitionResult {
    abstract fun toTable(): Table
}

