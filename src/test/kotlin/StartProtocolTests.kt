import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.athlete.*
import ru.emkn.kotlin.sms.startprotocol.GroupStartProtocol
import java.io.File
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal class StartProtocolTests {

    @Test
    fun testNumberAndStartTime() {
        val num = Random.nextInt(1, 59)
        val athlete = Athlete(
            Name("Данил Сибгатуллин"),
            Sex.MALE,
            LocalDate(2002, 12, 30),
            Category.I,
            GroupName("М2002"),
            TeamName("Б05"),
            GroupName("М2002"),
            AthleteNumber("$num")
        )
        val group = Group(Race(GroupName("М2002"), SportType.RUNNING), listOf(athlete))
        GroupStartProtocol(group, "src/test/resources/StartProtocolTests/")
        val list = csvReader().readAll(File("src/test/resources/StartProtocolTests/М2002.csv"))
        assertEquals(num.toString(), list[1][0])
        assertEquals("12:${num / 10}${num % 10}:00", list[1][5])
        File("src/test/resources/StartProtocolTests/М2002.csv").delete()
    }
}