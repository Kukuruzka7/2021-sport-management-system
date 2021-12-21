import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.datetime.LocalDate
import org.junit.Test
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.CompetitionSerialization
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.SportType
import ru.emkn.kotlin.sms.model.application.Application
import ru.emkn.kotlin.sms.model.athlete.Athlete
import ru.emkn.kotlin.sms.model.finishprotocol.FinishProtocol
import ru.emkn.kotlin.sms.model.finishprotocol.createDir
import ru.emkn.kotlin.sms.model.input_result.InputCompetitionResultByAthletes
import ru.emkn.kotlin.sms.model.result_data.ResultData
import ru.emkn.kotlin.sms.model.startprotocol.StartProtocol
import java.io.File
import kotlin.test.assertContentEquals

private const val dir = "src/main/resources/competitions/"

internal class TestOrienteering {

    private val direct = "src/test/testFiles/TestOrienteering/"

    private val names = listOf("Тимофей", "Данил", "Розалина", "Art")
    private val lastNames = listOf("Москаленко", "Сибгатуллин", "Миргалимова", "Petrofffff")
    private val years = 1900..2015
    private val sexes = listOf("Ж", "М")
    private val categories = listOf("Iю", "IIю", "IIIю", "I", "II", "III", "КМС", "МС", "ММС")
    private var iteration = 0

    private val result = mutableListOf<List<String>>()

    private fun generator(teamName: String) {
        csvWriter().open("$direct$teamName.csv") {
            writeRow(teamName, "", "", "", "")
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

    private fun competition(): Competition {
        createDir("src/main/resources/competitions/MNEPLOHO")
        Athlete.lastUsedNumber = 1
        repeat(20) {
            generator("teamApplication" + (it + 1).toString())
        }
        val apl = Application.create(
            List(20) {
                File("src/test/testFiles/TestOrienteering/teamApplication${it + 1}.csv")
            }
        )
        val comp = Competition(MetaInfo("MNEPLOHO", LocalDate(2021, 12, 20), SportType.ORIENTEERING), apl)
        StartProtocol(comp.groupList, "$dir${comp.info.name}/")
        comp.toCompetitionSerialization().save(
            "${dir}${comp.info.name}/competitionData.csv",
            "${dir}${comp.info.name}/competitionInfo.csv"
        )
        val data: List<List<String>> = try {
            csvReader().readAll(File("$dir${"MNEPLOHO"}/competitionData.csv"))
        } catch (e: Exception) {
            println("Что-то пошло не так, попробуйте ввести заявки заново.")
            null
        }!!
        val comp1 = Competition(CompetitionSerialization(data, listOf("MNEPLOHO", "2021-12-20", "ORIENTEERING")))
        val rows = comp1.athleteList.map {
            listOf(it.number.value) + List(it.race.checkPoints.size * 2) { i ->
                if (i % 2 == 0) {
                    it.race.checkPoints[i / 2].name
                } else {
                    "12:00:${(it.number.value.toInt() + i / 2 + 1) % 60 / 10}${(it.number.value.toInt() + i / 2 + 1) % 60 % 10}"
                }
            } + List(
                60 - it.race.checkPoints.size * 2
            ) { "" }
        }
        csvWriter().writeAll(rows, File("src/test/testFiles/TestOrienteering/ResultByAthletes.csv"))
        val athletesResults = ResultData(
            InputCompetitionResultByAthletes("src/test/testFiles/TestOrienteering/ResultByAthletes.csv").toTable(),
            CompetitionSerialization(data, listOf("MNEPLOHO", "2021-12-20", "ORIENTEERING")).getStartTime()
        )
        FinishProtocol(athletesResults, comp1)
        return comp1
    }

    @Test
    fun test1() {
        val comp1 = competition()
        assert(csvReader().readAll(File("src/main/resources/competitions/MNEPLOHO/finishProtocol/overallCSV.csv")).size == comp1.athleteList.size + comp1.groupList.size * 2 + 1)
    }

    @Test
    fun test2() {
        val comp1 = competition()
        val set = mutableSetOf<Int>()
        csvReader().readAll(File("src/main/resources/competitions/MNEPLOHO/finishProtocol/overallCSV.csv")).forEach {
            if (it[1].toIntOrNull() != null) {
                set.add(it[1].toInt())
            }
        }
        println(set.toList())
        println(comp1.athleteList.map { it.number.value.toInt() })
        assertContentEquals(set.toList().sorted(), comp1.athleteList.map { it.number.value.toInt() })
    }
}