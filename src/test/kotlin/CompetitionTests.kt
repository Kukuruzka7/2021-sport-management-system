import kotlinx.datetime.LocalDate
import org.junit.Test
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.SportType
import ru.emkn.kotlin.sms.model.application.Application
import ru.emkn.kotlin.sms.model.application.TeamApplication
import java.io.File
import kotlin.test.assertContentEquals

internal class CompetitionTests {

    @Test
    fun testGenerateTeamListByAthleteList() {
        val row1 = listOf("Розалина", "Миргалимова", "Ж", "2003", "КМС", "никуда не хочу")
        val row2 = listOf("Данил", "Сибгатуллин", "М", "2002", "МС", "не знаю")
        val row3 = listOf("Тимофей", "Москаленко", "М", "2004", "I", "никуда не хочу")
        val row4 = listOf("Импостер", "Импостерович", "М", "2003", "КМС", "ПЫТАЮСЬ БЫТЬ С НИМИ")
        val athlete1 = TeamApplication.processingRow(row1, "Команда мечты")
        val athlete2 = TeamApplication.processingRow(row2, "A")
        val athlete3 = TeamApplication.processingRow(row3, "B")
        val athlete4 = TeamApplication.processingRow(row4, "C")
        val athletes = listOf(athlete1, athlete2, athlete3, athlete4)
        assertContentEquals(
            Competition.generateTeamListByAthleteList(athletes).map { it.name },
            listOf("Команда мечты", "A", "B", "C")
        )
    }

    @Test
    fun testGenerateGroupListByAthleteList() {
        val row1 = listOf("Розалина", "Миргалимова", "Ж", "2003", "КМС", "никуда не хочу")
        val row2 = listOf("Данил", "Сибгатуллин", "М", "2002", "МС", "не знаю")
        val row3 = listOf("Тимофей", "Москаленко", "М", "2004", "I", "никуда не хочу")
        val row4 = listOf("Импостер", "Импостерович", "М", "2003", "КМС", "ПЫТАЮСЬ БЫТЬ С НИМИ")
        val athlete1 = TeamApplication.processingRow(row1, "Команда мечты")
        val athlete2 = TeamApplication.processingRow(row2, "A")
        val athlete3 = TeamApplication.processingRow(row3, "B")
        val athlete4 = TeamApplication.processingRow(row4, "C")
        val athletes = listOf(athlete1, athlete2, athlete3, athlete4)
        assertContentEquals(
            Competition.generateGroupListByAthleteList(athletes, SportType.RUNNING).map { it.race.groupName },
            listOf("Ж2003", "М2002", "М2004", "М2003")
        )
    }

    @Test
    fun testGroupDivision() {
        val row1 = listOf("Розалина", "Миргалимова", "Ж", "2003", "КМС", "никуда не хочу")
        val row2 = listOf("Данил", "Сибгатуллин", "М", "2002", "МС", "не знаю")
        val row3 = listOf("Тимофей", "Москаленко", "М", "2004", "I", "никуда не хочу")
        val row4 = listOf("Импостер", "Импостерович", "М", "2003", "КМС", "ПЫТАЮСЬ БЫТЬ С НИМИ")
        val athlete1 = TeamApplication.processingRow(row1, "Команда мечты")
        val athlete2 = TeamApplication.processingRow(row2, "A")
        val athlete3 = TeamApplication.processingRow(row3, "B")
        val athlete4 = TeamApplication.processingRow(row4, "C")
        val athletes = listOf(athlete1, athlete2, athlete3, athlete4)
        assertContentEquals(
            Competition.groupDivision(athletes, SportType.RUNNING).map { it.race.groupName },
            listOf("Ж2003", "М2002", "М2004", "М2003")
        )
    }

    @Test
    fun testCompetition() {
        val teamApplication1 = TeamApplication(File("src/test/testFiles/testTeamApplication/teamApplication1.csv"), 0)
        val teamApplication2 = TeamApplication(File("src/test/testFiles/testTeamApplication/teamApplication2.csv"), 0)
        val application = Application(
            listOf(
                File("src/test/testFiles/testTeamApplication/teamApplication1.csv"),
                File("src/test/testFiles/testTeamApplication/teamApplication2.csv")
            )
        )
        val competition = Competition(MetaInfo("Olympiad", LocalDate(2010, 10, 10), SportType.RUNNING), application)
        assert(competition.groupList.size == 3)
        assert(competition.teamList.size == 2)
        assert(competition.groupList.all { it.athletes.size == 2 })
    }
}