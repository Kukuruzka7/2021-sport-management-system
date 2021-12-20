import org.junit.Test
import ru.emkn.kotlin.sms.model.SportType

internal class SportTypeTests {

    @Test
    fun testSportType() {
        assert(SportType.RUNNING == SportType.get("бег"))
        assert(SportType.X == SportType.get("буг"))
        assert(SportType.RUNNING == SportType.get("running"))
        assert(SportType.SWIMMING == SportType.get("swimming"))
    }
}