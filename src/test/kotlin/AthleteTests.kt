import kotlinx.datetime.LocalDate
import org.junit.Test
import ru.emkn.kotlin.sms.model.athlete.Athlete
import ru.emkn.kotlin.sms.model.athlete.Category
import ru.emkn.kotlin.sms.model.athlete.Name
import ru.emkn.kotlin.sms.model.athlete.Sex

internal class AthleteTests {

    @Test
    fun testName() {
        val name1 = Name("Розалина", "Миргалимова")
        assert("Розалина Миргалимова" == name1.fullName)
        val name2 = Name("Вася Пупкин")
        assert("Пупкин" == name2.lastName)
        assert("Вася" == name2.firstName)
    }

    @Test
    fun testAthlete() {
        val athlete1 = Athlete(
            Name("Розалина Миргалимова"), Sex.FEMALE, LocalDate(2003, 1, 1), Category.CANDIDATE,
            "Ж2003",
            "Команда мечты",
           ("Ж2003")
        )
        assert(athlete1.name.fullName == "Розалина Миргалимова")
        assert(athlete1.groupName == "Ж2003")
    }

    @Test
    fun testCategoty() {
        assert(Category.getCategory("кмс") == Category.CANDIDATE)
        assert(Category.getCategory("мс") == Category.MASTER)
        assert(Category.getCategory("i") == Category.I)
        assert(Category.getCategory("aldskd") == Category.X)
    }

    @Test
    fun testSex() {
        assert(Sex.getSex("м") == Sex.MALE)
        assert(Sex.getSex("ж") == Sex.FEMALE)
        assert(Sex.getSex("М") == Sex.MALE)
        assert(Sex.getSex("aldskd") == Sex.X)
    }
}