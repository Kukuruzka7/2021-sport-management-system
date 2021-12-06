import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.junit.Test
import ru.emkn.kotlin.sms.SportType
import ru.emkn.kotlin.sms.TeamName
import ru.emkn.kotlin.sms.application.Application
import ru.emkn.kotlin.sms.application.TeamApplication
import ru.emkn.kotlin.sms.athlete.Athlete
import ru.emkn.kotlin.sms.athlete.Category
import ru.emkn.kotlin.sms.start
import java.io.File
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class ApplicationGeneratorTests {

    val dir = "src/test/resources/ApplicationGeneratorTests/"

    val names = listOf("Тимофей", "Данил", "Розалина", "Art")
    val lastNames = listOf("Москаленко", "Сибгатуллин", "Миргалимова", "Petrofffff")
    val years = 1900..2015
    val sexes = listOf("Ж", "М")
    val categories = listOf("Iю", "IIю", "IIIю", "I", "II", "III", "КМС", "МС", "ММС")
    var iteration = 0

    val result = mutableListOf<List<String>>()

    fun generator(teamName: TeamName) {
        csvWriter().open(dir + teamName.name + ".csv") {
            writeRow(teamName.name, "", "", "", "")
            writeRow("Фамилия", "Имя", "Пол", "Год рождения", "Спорт категория")
            repeat(20) {
                iteration++
                val name = names.random()
                val lastName = lastNames.random()
                val sex = sexes.random()
                val year = years.random()
                val category = categories.random()
                val hours = (12 + iteration / 60) % 24
                val minutes = iteration % 60
                result.add(
                    listOf(
                        iteration.toString(),
                        "$lastName $name",
                        sex,
                        "$year-01-01",
                        category,
                        ((iteration - 1) / 20).toString(),
                        "$sex$year",
                        "TODO()",
                        "${hours / 10}${hours % 10}:${minutes / 10}${minutes % 10}"
                    )
                )
                writeRow(
                    name,
                    lastName,
                    sex,
                    year,
                    category
                )
            }
        }
    }

    @Test
    fun testStart() {
        Athlete.lastUsedNumber = 1
        val args = arrayOf(
            "",
            "testStart",
            "running",
            "2021-11-29",
            "src/test/resources/ApplicationGeneratorTests/ApplicationsNames"
        )
        repeat(20) {
            generator(TeamName(it.toString()))
        }
        start(args)
        assertEquals(csvReader().readAll(File("src/main/resources/competitions/testStart/competitionData.csv")), result.toList())
    }
}
