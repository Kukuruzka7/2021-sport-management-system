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
    private val groupPath: String = "$path/${group.name.name}.csv"
    val toCSV: Any = writeGroupToCSV()

    private fun writeGroupToCSV() {
        val startTime = GregorianCalendar()
        var num = 1
        startTime.set(2021, 12, 1, 12, 0, 0)
        File(groupPath).createNewFile()
        csvWriter().open(groupPath) {

            writeRow(group.name.name, "Фамилия", "Имя", "Год рождения", "Разряд", "Время старта")
            group.athletes.forEach { athlete ->
                writeRow(
                    "${group.name.name}-$num",
                    athlete.name.firstName,
                    athlete.name.secondName,
                    athlete.birthDate?.year,
                    athlete.sportCategory.categoryName,
                    "${startTime.get(11) / 10}${startTime.get(11) % 10}:" +
                            "${startTime.get(12) / 10}${startTime.get(12) % 10}:" +
                            "${startTime.get(13) / 10}${startTime.get(13) % 10}",
                )
                num++
                startTime.add(GregorianCalendar.MINUTE, 1)
            }
        }
    }
}