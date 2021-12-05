import kotlinx.datetime.LocalDate
import org.junit.Test
import ru.emkn.kotlin.sms.GroupName
import ru.emkn.kotlin.sms.TeamName
import ru.emkn.kotlin.sms.athlete.Athlete
import ru.emkn.kotlin.sms.athlete.Category
import ru.emkn.kotlin.sms.athlete.Name
import ru.emkn.kotlin.sms.athlete.Sex

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
        val athlete1 = Athlete(Name("Розалина Миргалимова"), Sex.FEMALE, LocalDate(2003,1,1), Category.CANDIDATE, GroupName("Ж2003") , TeamName("Команда мечты"),  GroupName("Ж2003"))
        assert(athlete1.name.fullName=="Розалина Миргалимова")
        assert(athlete1.groupName.value=="Ж2003")
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