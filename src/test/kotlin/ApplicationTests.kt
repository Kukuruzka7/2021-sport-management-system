import org.junit.Test
import ru.emkn.kotlin.sms.SportType
import ru.emkn.kotlin.sms.TeamName
import ru.emkn.kotlin.sms.application.Application
import ru.emkn.kotlin.sms.application.TeamApplication
import ru.emkn.kotlin.sms.finishprotocol.minus
import ru.emkn.kotlin.sms.startprotocol.toStringWithSeconds
import java.io.File
import java.time.LocalTime
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class ApplicationTests {

    @Test
    fun testTeamApplication() {
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
}