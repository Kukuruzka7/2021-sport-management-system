package ru.emkn.kotlin.sms.startprotocol

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.datetime.Clock
import kotlinx.datetime.toLocalDateTime
import ru.emkn.kotlin.sms.Group
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class StartProtocol(listGroup: List<Group>) {
    val generateCSV: List<GroupStartProtocol>

    init {
        generateCSV = listGroup.map { GroupStartProtocol(it) }
    }
}

class GroupStartProtocol(val group: Group) {
    val toCSV: Any = writeGroupToCSV()

    private fun writeGroupToCSV() {
        var startTime = GregorianCalendar()
        startTime.set(2004, 3, 24, 12,0, 0)
        csvWriter().open("src/main/resources/test.csv") {

            writeRow(group.type, "Фамилия", "Имя", "Год рождения", "Разряд", "Время старта")
            group.athletes.forEach { athlete ->
                writeRow(
                    athlete.number,
                    athlete.name.firstName,
                    athlete.name.secondName,
                    athlete.birthDate.year,
                    athlete.sportCategory,
                    startTime.toString(),
                )
                startTime.add(GregorianCalendar.MINUTE, 1)
            }
        }
    }
}