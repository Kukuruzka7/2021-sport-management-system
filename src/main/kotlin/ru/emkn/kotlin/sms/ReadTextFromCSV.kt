package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File

fun readCSV(fileName: String): List<List<String>> {
    val file = File(fileName)
    if (!file.exists()) {
        throw FileDoesNotExist(fileName)
    }
    return try {
        csvReader().readAll(file.readText())
    } catch (_: Exception) {
        throw InvalidCSV(fileName)
    }
}