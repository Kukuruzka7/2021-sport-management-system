package ru.emkn.kotlin.sms.startprotocol

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import ru.emkn.kotlin.sms.Group
import java.io.File
import java.util.*

class GroupStartProtocol(private val group: Group, path: String) {
    private val groupPath: String = "$path/${group.race.groupName}.csv"
    val toCSV = writeGroupToCSV()

    private fun writeGroupToCSV() {
        // TODO("Стандартная реализация времени")
        val startTime = GregorianCalendar()
        startTime.set(2021, 12, 1, 12, 0, 0)

        File(groupPath).createNewFile()
        csvWriter().open(groupPath) {
            writeRow(group.race.groupName, "Фамилия", "Имя", "Год рождения", "Разряд", "Время старта")
            group.athletes.forEach { athlete ->
                writeRow(
                    athlete.number,
                    athlete.name.firstName,
                    athlete.name.lastName,
                    athlete.birthDate?.year,
                    athlete.sportCategory.name,
                    startTime.timeToString(),
                )
                startTime.add(GregorianCalendar.MINUTE, 1)
            }
        }
    }

    private fun GregorianCalendar.timeToString(): String {
        return "${this.get(11) / 10}${this.get(11) % 10}:" +
                "${this.get(12) / 10}${this.get(12) % 10}:" +
                "${this.get(13) / 10}${this.get(13) % 10}"
    }
}