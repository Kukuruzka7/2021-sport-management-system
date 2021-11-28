import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.athlete.*
import ru.emkn.kotlin.sms.finishprotocol.*
import ru.emkn.kotlin.sms.result_data.Checkpoint
import ru.emkn.kotlin.sms.result_data.CheckpointRes
import ru.emkn.kotlin.sms.result_data.ResultData
import ru.emkn.kotlin.sms.result_data.Table
import java.io.File
import java.time.LocalTime
import kotlin.test.*

internal class FinishProtocolTests {

    @Test
    fun test1() {
        sport = SportType.RUNNING

        val resData = ResultData(
            Table(
                mapOf(
                    AthleteNumber("1") to listOf(
                        CheckpointRes(
                            Checkpoint("finish"),
                            AthleteNumber("1"),
                            LocalTime.of(12, 2, 0)
                        )
                    )
                )
            ),
            mapOf(AthleteNumber("1") to LocalTime.of(12, 1, 0))
        )
        createDir("src/main/resources/competitions/123/")
        val comp = Competition(
            CompetitionData(
                listOf(
                    listOf("1", "Сибгатуллин Данил", "М", "2002-12-30", "I", "Б05", "М2002", "TODO()", "12:01")
                ),
                listOf("123", "2021-11-27", "RUNNING")
            )
        )

        FinishProtocol(resData, comp)

        assertEquals(
            csvReader().readAll(File("src/main/resources/competitions/123/finishProtocol/groups/М2002.csv")),
            listOf(
                listOf("М2002", "", "", "", "", "", "", "", "", ""),
                listOf(
                    "№ п/п",
                    "Номер",
                    "Фамилия",
                    "Имя",
                    "Г.р.",
                    "Разр.",
                    "Команда",
                    "Результат",
                    "Место",
                    "Отставание"
                ),
                listOf("1", "1", "Данил", "Сибгатуллин", "2002", "IIIю", "Б05", "00:01:00", "1", "")
            )
        )
        File("src/main/resources/competitions/123/finishProtocol/groups/М2002.csv").delete()
        assertEquals(
            csvReader().readAll(File("src/main/resources/competitions/123/finishProtocol/teams/Б05.csv")),
            listOf(
                listOf("Б05", "", "", "", "", "", "", "", "", ""),
                listOf(
                    "№ п/п",
                    "Номер",
                    "Фамилия",
                    "Имя",
                    "Г.р.",
                    "Разр.",
                    "Команда",
                    "Результат",
                    "Место",
                    "Отставание"
                ),
                listOf("1", "1", "Данил", "Сибгатуллин", "2002", "IIIю", "Б05", "00:01:00", "1", "")
            )
        )
        File("src/main/resources/competitions/123/finishProtocol/teams/Б05.csv").delete()
        assertEquals(
            csvReader().readAll(File("src/main/resources/competitions/123/finishProtocol/overallCSV.csv")),
            listOf(
                listOf("Общий протокол", "", "", "", "", "", "", "", "", ""),
                listOf("М2002", "", "", "", "", "", "", "", "", ""),
                listOf(
                    "№ п/п",
                    "Номер",
                    "Фамилия",
                    "Имя",
                    "Г.р.",
                    "Разр.",
                    "Команда",
                    "Результат",
                    "Место",
                    "Отставание"
                ),
                listOf("1", "1", "Данил", "Сибгатуллин", "2002", "IIIю", "Б05", "00:01:00", "1", "")
            )
        )
        File("src/main/resources/competitions/123/finishProtocol/overallCSV.csv").delete()
        File("src/main/resources/competitions/123/finishProtocol/groups/").delete()
        File("src/main/resources/competitions/123/finishProtocol/teams/").delete()
        File("src/main/resources/competitions/123/finishProtocol/").delete()
        File("src/main/resources/competitions/123/finishProtocol/").delete()
        File("src/main/resources/competitions/123/").delete()
    }
}