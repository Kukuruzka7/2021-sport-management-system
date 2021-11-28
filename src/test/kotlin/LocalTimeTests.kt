import java.time.LocalTime
import kotlin.test.*
import ru.emkn.kotlin.sms.finishprotocol.*
import ru.emkn.kotlin.sms.startprotocol.*

internal class LocalTimeTests {

    private val time0 = LocalTime.of(0, 0, 0)
    private val time1 = LocalTime.of(1, 23, 45)
    private val time2 = LocalTime.of(6, 54, 32)
    private val time3 = LocalTime.of(23, 59, 59)

    @Test
    fun testMinus() {
        assertEquals(time1 - time0, time1)
        assertEquals(time2 - time0, time2)
        assertEquals(time3 - time0, time3)
        assertEquals(time1 - time1, time0)
        assertEquals(time2 - time2, time0)
        assertEquals(time3 - time3, time0)
        assertEquals(time2 - time1, LocalTime.of(5,30,47))
        assertEquals(time1 - time2, LocalTime.of(18,29,13))
        assertEquals(time0 - time3, LocalTime.of(0,0,1))
    }
    @Test
    fun testToString() {
        assertEquals(time0.toStringWithSeconds(), "00:00:00")
        assertEquals(time1.toStringWithSeconds(), "01:23:45")
        assertEquals(time2.toStringWithSeconds(), "06:54:32")
        assertEquals(time3.toStringWithSeconds(), "23:59:59")
    }
}