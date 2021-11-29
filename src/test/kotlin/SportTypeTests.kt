import org.junit.Test
import ru.emkn.kotlin.sms.SportType
import ru.emkn.kotlin.sms.athlete.Name

internal class SportTypeTests {

    @Test
    fun testSportType() {
        assert(SportType.RUNNING == SportType.getSportType("бег"))
        assert(SportType.X == SportType.getSportType("буг"))
        assert(SportType.RUNNING == SportType.getSportType("running"))
        assert(SportType.SWIMMING == SportType.getSportType("swimming"))
    }
}