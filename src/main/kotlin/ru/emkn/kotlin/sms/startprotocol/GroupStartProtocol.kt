package ru.emkn.kotlin.sms.startprotocol

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import ru.emkn.kotlin.sms.Group
import java.io.File

class GroupStartProtocol(private val group: Group, path: String) {
    private val groupPath: String = "$path${group.race.groupName}.csv"
    val toCSV = writeGroupToCSV()

    private fun writeGroupToCSV() {
        val timeZone = TimeZone.of("Europe/Moscow")
        var startTime = LocalDateTime(2021, 12, 1, 12, 0, 0)

        File(groupPath).createNewFile()
        csvWriter().open(groupPath) {
            writeRow(group.race.groupName, "Фамилия", "Имя", "Год рождения", "Разряд", "Время старта")
            group.athletes.forEach { athlete ->
                writeRow(
                    athlete.number.toString(),
                    athlete.name.firstName,
                    athlete.name.lastName,
                    athlete.birthDate.year,
                    athlete.sportCategory.toString(),
                    startTime.toString(),
                )
                val instant = startTime.toInstant(timeZone)
                val instantOneMinuteLater = instant.plus(1, DateTimeUnit.MINUTE, timeZone)
                startTime = instantOneMinuteLater.toLocalDateTime(timeZone)
            }
        }
    }
}

