import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.athlete.*
import kotlin.test.Test
import ru.emkn.kotlin.sms.application.*
import ru.emkn.kotlin.sms.application.TeamApplication.Companion.checkFormatOfApplication
import ru.emkn.kotlin.sms.application.TeamApplication.Companion.checkRow
import ru.emkn.kotlin.sms.application.TeamApplication.Companion.processingData
import ru.emkn.kotlin.sms.application.TeamApplication.Companion.processingRow
import java.io.File
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class TeamApplicationTests {

    @Test
    fun testCheckRow() {
        val row1 = listOf("Розалина", "Миргалимова", "Ж", "2003", "КМС", "никуда не хочу")
        val row2 = listOf("Розалина", "Миргалимова", "Ж", "2003")
        val row3 = listOf("Розалина", "Миргалимова", "A", "2003", "КМС", "никуда не хочу")
        val row4 = listOf("Розалина", "Миргалимова", "Ж", "sdjfh", "КМС", "никуда не хочу")
        val row5 = listOf("Розалина", "Миргалимова", "Ж", "2003", "КМС", "никуда не хочу")
        val row6 = listOf("Розалина", "Миргалимова", "Ж", "2003", "КЕК", "никуда не хочу")
        val row7 = listOf("Розалина", "Ж", "2003", "КМС", "никуда не хочу")
        val row8 = listOf("Розалина", "Миргалимова", "Ь", "2003", "КМС", "никуда не хочу")
        val row9 = listOf("Розалина", "Миргалимова", "Ж", "2003", "КМС")
        checkRow(row1, 0)
        try {
            checkRow(row2, 0)
        } catch (e: Exception) {
            assert(e is ApplicationHasWrongFormatOnLine)
        }
        try {
            checkRow(row3, 0)
        } catch (e: Exception) {
            assert(e is WrongSexInApplicationOnLine)
        }
        try {
            checkRow(row4, 0)
        } catch (e: Exception) {
            assert(e is WrongYearInApplicationOnLine)
        }
        checkRow(row5, 0)
        try {
            checkRow(row6, 0)
        } catch (e: Exception) {
            assert(e is WrongCategoryInApplicationOnLine)
        }
        try {
            checkRow(row7, 0)
        } catch (e: Exception) {
            assert(e is WrongSexInApplicationOnLine)
        }
        try {
            checkRow(row8, 0)
        } catch (e: Exception) {
            assert(e is WrongSexInApplicationOnLine)
        }
        checkRow(row9, 0)
    }

    @Test
    fun testCheckFormatOfApplication() {
        val row1 = listOf("Розалина", "Миргалимова", "Ж", "2003", "КМС", "никуда не хочу")
        val row2 = listOf("Розалина", "Миргалимова", "Ж", "2003")
        val row3 = listOf("Розалина", "Миргалимова", "A", "2003", "КМС", "никуда не хочу")
        val row4 = listOf("Розалина", "Миргалимова", "Ж", "sdjfh", "КМС", "никуда не хочу")
        val row5 = listOf("Розалина", "Миргалимова", "Ж", "2003", "КМС", "никуда не хочу")
        val row6 = listOf("Розалина", "Миргалимова", "Ж", "2003", "КЕК", "никуда не хочу")
        val row8 = listOf("Розалина", "Миргалимова", "Ь", "2003", "КМС", "никуда не хочу")
        val row9 = listOf("Розалина", "Миргалимова", "Ж", "2003", "КМС")
        val apl1 = listOf(row1, row1, row1)
        val apl2 = listOf(row1, row2, row9)
        val apl3 = listOf(row1, row3, row9)
        val apl4 = listOf(row1, row4, row9)
        val apl5 = listOf(row5, row9, row9)
        val apl6 = listOf(row9, row8, row1)
        val apl7 = listOf(row6, row1, row1)
        checkFormatOfApplication(0, apl1)
        try {
            checkFormatOfApplication(0, apl2)
        } catch (e: Exception) {
            assert(e is ApplicationHasWrongFormatOnLine)
        }
        try {
            checkFormatOfApplication(0, apl3)
        } catch (e: Exception) {
            assert(e is ApplicationHasWrongFormatOnLine)
        }
        try {
            checkFormatOfApplication(0, apl4)
        } catch (e: Exception) {
            assert(e is WrongYearInApplicationOnLine)
        }
        checkFormatOfApplication(0, apl5)
        try {
            checkFormatOfApplication(0, apl6)
        } catch (e: Exception) {
            assert(e is WrongSexInApplicationOnLine)
        }
        try {
            checkFormatOfApplication(0, apl7)
        } catch (e: Exception) {
            assert(e is WrongCategoryInApplicationOnLine)
        }
    }

    @Test
    fun testProcessingRow() {
        sport = SportType.RUNNING
        val row1 = listOf("Розалина", "Миргалимова", "Ж", "2003", "КМС", "никуда не хочу")
        val athlete1 = processingRow(row1, TeamName("Команда мечты"))
        assert(athlete1.name.fullName == "Миргалимова Розалина")
        assert(athlete1.sex == Sex.FEMALE)
        assert(athlete1.teamName.name == "Команда мечты")
        assert(athlete1.birthDate == LocalDate(2003, 1, 1))
        assert(athlete1.groupName.value == "Ж2003")
        assert(athlete1.sportCategory == Category.CANDIDATE)
    }

    @Test
    fun testProcessingData() {
        sport = SportType.RUNNING
        val row1 = listOf("Розалина", "Миргалимова", "Ж", "2003", "КМС", "никуда не хочу")
        val row2 = listOf("Данил", "Сибгатуллин", "М", "2002", "МС", "не знаю")
        val row3 = listOf("Тимофей", "Москаленко", "М", "2004", "I", "никуда не хочу")
        val row4 = listOf("Импостер", "Импостерович", "М", "2003", "КМС", "ПЫТАЮСЬ БЫТЬ С НИМИ")
        val athlete1 = processingRow(row1, TeamName("Команда мечты"))
        val athlete2 = processingRow(row2, TeamName("Команда мечты"))
        val athlete3 = processingRow(row3, TeamName("Команда мечты"))
        val athlete4 = processingRow(row4, TeamName("Команда мечты"))
        val athletes = processingData(listOf(row1, row2, row3, row4), TeamName("Команда мечты"))
        assertEquals(athlete1, athletes[0])
        assertEquals(athlete2, athletes[1])
        assertEquals(athlete3, athletes[2])
        assertEquals(athlete4, athletes[3])
    }

    @Test
    fun testTeamApplication() {
        sport = SportType.RUNNING
        val teamApplication = TeamApplication(File("src/test/testFiles/testTeamApplication/teamApplication1.csv"), 0)
        val row1 = listOf("Миргалимова", "Розалина", "Ж", "2003", "КМС", "никуда не хочу")
        val row2 = listOf("Сибгатуллин", "Данил", "М", "2002", "МС", "не знаю")
        val row3 = listOf("Москаленко", "Тимофей", "М", "2004", "I", "никуда не хочу")
        val athlete1 = processingRow(row1, TeamName("Команда мечты"))
        val athlete2 = processingRow(row2, TeamName("Команда мечты"))
        val athlete3 = processingRow(row3, TeamName("Команда мечты"))
        assertContentEquals(listOf(athlete1, athlete2, athlete3), teamApplication.team.athletes)
    }
}
