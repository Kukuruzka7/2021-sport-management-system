import org.junit.Test
import ru.emkn.kotlin.sms.Race.Companion.getCheckPoints
import ru.emkn.kotlin.sms.SportType
import ru.emkn.kotlin.sms.application.Application
import ru.emkn.kotlin.sms.application.TeamApplication
import ru.emkn.kotlin.sms.result_data.Checkpoint
import ru.emkn.kotlin.sms.sport
import java.io.File
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class GroupTests {

    @Test
    fun testRace() {
        sport = SportType.RUNNING
        val teamApplication1 = TeamApplication(File("src/test/testFiles/testTeamApplication/teamApplication1.csv"), 0)
        val teamApplication2 = TeamApplication(File("src/test/testFiles/testTeamApplication/teamApplication2.csv"), 0)
        val application = Application(
            listOf(
                File("src/test/testFiles/testTeamApplication/teamApplication1.csv"),
                File("src/test/testFiles/testTeamApplication/teamApplication2.csv")
            )
        )
        assertContentEquals(application.teamApplicationsList[0].team.athletes, teamApplication1.team.athletes)
        assertContentEquals(application.teamApplicationsList[1].team.athletes, teamApplication2.team.athletes)
    }

    @Test
    fun testGetCheckPoints() {
        sport = SportType.RUNNING
        val checkPoints1 = getCheckPoints("М2000")
        assertEquals(listOf(Checkpoint("100"), Checkpoint("200"), Checkpoint("300"), Checkpoint("400")), checkPoints1)
        val checkPoints2 = getCheckPoints("М2003")
        assertEquals(listOf(Checkpoint("200"), Checkpoint("400"), Checkpoint("600"), Checkpoint("800")), checkPoints2)
        val checkPoints3 = getCheckPoints("Ж2000")
        assertEquals(listOf(Checkpoint("30")), checkPoints3)
        val checkPoints4 = getCheckPoints("Ж2003")
        assertEquals(listOf(Checkpoint("60")), checkPoints4)
    }
}