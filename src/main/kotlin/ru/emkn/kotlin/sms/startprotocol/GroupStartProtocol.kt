package ru.emkn.kotlin.sms.startprotocol

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import ru.emkn.kotlin.sms.Group
import java.io.File
import java.time.LocalTime as LocalTime

class GroupStartProtocol(private val group: Group, path: String) {
    private val groupPath: String = "$path${group.race.groupName}.csv"
    val toCSV = writeGroupToCSV()

    private fun writeGroupToCSV() {
        File(groupPath).createNewFile()
        csvWriter().open(groupPath) {
            writeRow(group.race.groupName, "Фамилия", "Имя", "Год рождения", "Разряд", "Время старта")
            group.athletes.forEach { athlete ->
                val startTime = LocalTime.of( 12, 0, 0).plusMinutes(athlete.number.value.toLongOrNull() ?: 0)
                writeRow(
                    athlete.number.toString(),
                    athlete.name.firstName,
                    athlete.name.lastName,
                    athlete.birthDate.year,
                    athlete.sportCategory.toString(),
                    startTime.toString(withSecond = true),
                )
                athlete.startTime = startTime
            }
        }
    }
}
fun LocalTime.toString(withSecond : Boolean): String =
    this.toString() + if(withSecond) ":${this.second / 10}${this.second % 10}" else ""