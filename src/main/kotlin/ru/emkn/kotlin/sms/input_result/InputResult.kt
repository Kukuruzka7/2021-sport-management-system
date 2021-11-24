package ru.emkn.kotlin.sms.input_result

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.ResultCanNotBeRead
import java.io.File

interface InputResult {
    val fileName: String
    fun getRows(): List<List<String>> = try {
        csvReader().readAll(File(fileName).toString())
    } catch (_: Exception) {
        throw ResultCanNotBeRead(fileName)
    }
}