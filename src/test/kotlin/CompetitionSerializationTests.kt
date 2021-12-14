import org.junit.Test
import ru.emkn.kotlin.sms.model.CompetitionSerialization
import ru.emkn.kotlin.sms.model.TeamName
import ru.emkn.kotlin.sms.model.application.TeamApplication
import kotlin.test.assertContentEquals

internal class CompetitionSerializationTests {

    @Test
    fun testCompetitionData() {
        val row1 =
            listOf("1", "Розалина Миргалимова", "Ж", "2003-01-01", "КМС", "A", "Ж2003", "никуда не хочу", "12:00:00")
        val row2 = listOf("2", "Данил Сибгатуллин", "М", "2002-01-01", "МС", "B", "М2002", "не знаю", "12:00:01")
        val row3 = listOf("3", "Тимофей Москаленко", "М", "2004-01-01", "I", "C", "М2004", "никуда не хочу", "12:00:02")
        val row4 =
            listOf(
                "4",
                "Импостер Импостерович",
                "М",
                "2003-01-01",
                "КМС",
                "D",
                "М2003",
                "ПЫТАЮСЬ БЫТЬ С НИМИ",
                "12:00:03"
            )
        CompetitionSerialization(listOf(row1, row2, row3, row4), listOf("Olympiad", "2021-10-10", "RUNNING"))
    }

    @Test
    fun testSave() {
        val row1 =
            listOf("1", "Розалина Миргалимова", "Ж", "2003-01-01", "КМС", "A", "Ж2003", "никуда не хочу", "12:00:00")
        val row2 = listOf("2", "Данил Сибгатуллин", "М", "2002-01-01", "МС", "B", "М2002", "не знаю", "12:00:01")
        val row3 = listOf("3", "Тимофей Москаленко", "М", "2004-01-01", "I", "C", "М2004", "никуда не хочу", "12:00:02")
        val row4 =
            listOf(
                "4",
                "Импостер Импостерович",
                "М",
                "2003-01-01",
                "КМС",
                "D",
                "М2003",
                "ПЫТАЮСЬ БЫТЬ С НИМИ",
                "12:00:03"
            )
        val competitionSerializator =
            CompetitionSerialization(listOf(row1, row2, row3, row4), listOf("Olympiad", "2021-10-10", "RUNNING"))
        competitionSerializator.save(
            "src/test/testFiles/testCompetitionData/competitionData.csv",
            "src/test/testFiles/testCompetitionData/competitionInfo.csv"
        )
    }

    @Test
    fun testToAthleteList() {
        val row1 =
            listOf("1", "Розалина Миргалимова", "Ж", "2003-01-01", "КМС", "A", "Ж2003", "никуда не хочу", "12:00:00")
        val row2 = listOf("2", "Данил Сибгатуллин", "М", "2002-01-01", "МС", "B", "М2002", "не знаю", "12:00:01")
        val row3 = listOf("3", "Тимофей Москаленко", "М", "2004-01-01", "I", "C", "М2004", "никуда не хочу", "12:00:02")
        val row4 =
            listOf(
                "4",
                "Импостер Импостерович",
                "М",
                "2003-01-01",
                "КМС",
                "D",
                "М2003",
                "ПЫТАЮСЬ БЫТЬ С НИМИ",
                "12:00:03"
            )
        val athlete1 = TeamApplication.processingRow(
            listOf("Миргалимова", "Розалина", "Ж", "2003", "КМС", "никуда не хочу"),
            TeamName("A")
        )
        val athlete2 =
            TeamApplication.processingRow(listOf("Сибгатуллин", "Данил", "М", "2002", "МС", "не знаю"), TeamName("B"))
        val athlete3 = TeamApplication.processingRow(
            listOf("Москаленко", "Тимофей", "М", "2004", "I", "никуда не хочу"),
            TeamName("C")
        )
        val athlete4 = TeamApplication.processingRow(
            listOf("Импостерович", "Импостер", "М", "2003", "КМС", "ПЫТАЮСЬ БЫТЬ С НИМИ"),
            TeamName("D")
        )
        assertContentEquals(
            CompetitionSerialization(
                listOf(row1, row2, row3, row4),
                listOf("Olympiad", "2021-10-10", "RUNNING")
            ).toAthletesList(),
            listOf(athlete1, athlete2, athlete3, athlete4)
        )
    }
}