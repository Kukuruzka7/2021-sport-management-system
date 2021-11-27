package ru.emkn.kotlin.sms.input_result

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.ResultCanNotBeRead
import java.io.File

abstract class InputResult {
    abstract val list: List<String>
}