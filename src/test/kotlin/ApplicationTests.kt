import org.junit.Test
import ru.emkn.kotlin.sms.model.application.Application
import ru.emkn.kotlin.sms.model.application.TeamApplication
import java.io.File
import kotlin.test.assertContentEquals

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