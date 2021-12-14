import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.CompetitionData
import ru.emkn.kotlin.sms.model.athlete.AthleteNumber
import ru.emkn.kotlin.sms.model.finishprotocol.FinishProtocol
import ru.emkn.kotlin.sms.model.finishprotocol.createDir
import ru.emkn.kotlin.sms.model.result_data.Checkpoint
import ru.emkn.kotlin.sms.model.result_data.CheckpointRes
import ru.emkn.kotlin.sms.model.result_data.ResultData
import ru.emkn.kotlin.sms.model.result_data.Table
import java.io.File
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals

internal class FinishProtocolTests {

    @Test
    fun test1() {

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
                    listOf("1", "Сибгатуллин Данил", "М", "2002-12-30", "IIIю", "Б05", "М2002", "TODO()", "12:01")
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
        assert(File("src/main/resources/competitions/123/finishProtocol/overallCSV.csv").delete())
        assert(File("src/main/resources/competitions/123/finishProtocol/groups/").delete())
        assert(File("src/main/resources/competitions/123/finishProtocol/teams/").delete())
        assert(File("src/main/resources/competitions/123/finishProtocol/").delete())
        assert(File("src/main/resources/competitions/123/").delete())
    }
}