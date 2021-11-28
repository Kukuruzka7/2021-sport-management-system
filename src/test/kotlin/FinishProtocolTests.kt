import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.athlete.*
import ru.emkn.kotlin.sms.finishprotocol.FinishProtocol
import ru.emkn.kotlin.sms.result_data.Checkpoint
import ru.emkn.kotlin.sms.result_data.CheckpointRes
import ru.emkn.kotlin.sms.result_data.ResultData
import ru.emkn.kotlin.sms.result_data.Table
import ru.emkn.kotlin.sms.startprotocol.GroupStartProtocol
import java.io.File
import java.time.LocalTime
import kotlin.random.Random
import kotlin.test.*

internal class FinishProtocolTests {

    @Test
    fun test1() {
        sport = SportType.RUNNING
        val athlete = Athlete(
            Name("Данил Сибгатуллин"),
            Sex.MALE,
            LocalDate(2002, 12, 30),
            Category.I,
            GroupName("М2002"),
            TeamName("Б05"),
            GroupName("М2002"),
            AthleteNumber("1")
        )
        val group = Group(Race(GroupName("М2002")), listOf(athlete))

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

        val comp = Competition(CompetitionData(listOf(listOf("1","Сибгатуллин Данил","М","2002-12-30","I","Б05","М2002","TODO()","12:01"
        )), listOf("","2021-11-27","RUNNING")))

        FinishProtocol(resData, comp)
        val list = csvReader().readAll(File("src/test/resources/StartProtocolTests/М2002.csv"))
    }
}