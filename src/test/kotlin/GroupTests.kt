import org.junit.Test
import ru.emkn.kotlin.sms.Race.Companion.getCheckPoints
import ru.emkn.kotlin.sms.model.SportType
import ru.emkn.kotlin.sms.model.application.Application
import ru.emkn.kotlin.sms.model.application.TeamApplication
import ru.emkn.kotlin.sms.model.result_data.Checkpoint
import java.io.File
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class GroupTests {

    @Test
    fun testRace() {
        val teamApplication1 = TeamApplication(File("src/test/testFiles/testTeamApplication/teamApplication1.csv"), 0)
        val teamApplication2 = TeamApplication(File("src/test/testFiles/testTeamApplication/teamApplication2.csv"), 0)
        val application = Application(
            listOf(
                File("src/test/testFiles/testTeamApplication/teamApplication1.csv"),
                File("src/test/testFiles/testTeamApplication/teamApplication2.csv")
            )
        )
        assertContentEquals(application.teamApplications[0].team.athletes, teamApplication1.team.athletes)
        assertContentEquals(application.teamApplications[1].team.athletes, teamApplication2.team.athletes)
    }

    @Test
    fun testGetCheckPoints() {
        val checkPoints1 = getCheckPoints("М2000", SportType.RUNNING)
        assertEquals(listOf(Checkpoint("100"), Checkpoint("200"), Checkpoint("300"), Checkpoint("400")), checkPoints1)
        val checkPoints2 = getCheckPoints("М2003", SportType.RUNNING)
        assertEquals(listOf(Checkpoint("200"), Checkpoint("400"), Checkpoint("600"), Checkpoint("800")), checkPoints2)
        val checkPoints3 = getCheckPoints("Ж2000", SportType.RUNNING)
        assertEquals(listOf(Checkpoint("30")), checkPoints3)
        val checkPoints4 = getCheckPoints("Ж2003", SportType.RUNNING)
        assertEquals(listOf(Checkpoint("60")), checkPoints4)
    }
}