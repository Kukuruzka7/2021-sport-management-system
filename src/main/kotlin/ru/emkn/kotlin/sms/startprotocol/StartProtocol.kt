package ru.emkn.kotlin.sms.startprotocol

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import ru.emkn.kotlin.sms.Group
import java.io.File
import java.util.*

class StartProtocol(listGroup: List<Group>, name: String) {
    private val generateCSV: List<GroupStartProtocol>
    private val path: String

    init {
        path = "src/main/resources/competitions/$name"
        // Нам рассказывали, что с фалом могут произойти какие-то беды, поэтому нужно проверять прям на месте
        if (!File(path).exists()) {
            try {
                File(path).mkdir()
            } catch (e: Exception) {
                throw Exception("Не получилось создать директорию $path")
            }
        }

        generateCSV = listGroup.map { GroupStartProtocol(it, path) }
        generateCSV.forEach { it.toCSV }
    }
}

class GroupStartProtocol(private val group: Group, path: String) {
    private val groupPath: String = "$path/${group.race.groupName}.csv"
    val toCSV = writeGroupToCSV()

    private fun writeGroupToCSV() {
        val startTime = GregorianCalendar()
        startTime.set(2021, 12, 1, 12, 0, 0)
        File(groupPath).createNewFile()
        csvWriter().open(groupPath) {
            writeRow(group.race.groupName, "Фамилия", "Имя", "Год рождения", "Разряд", "Время старта")
            group.athletes.forEach { athlete ->
                writeRow(
                    "${group.race.groupName}-${athlete.number}",
                    athlete.name.firstName,
                    athlete.name.secondName,
                    athlete.birthDate?.year,
                    athlete.sportCategory.categoryName,
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