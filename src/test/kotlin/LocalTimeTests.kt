import ru.emkn.kotlin.sms.finishprotocol.minus
import ru.emkn.kotlin.sms.finishprotocol.toInt
import ru.emkn.kotlin.sms.finishprotocol.toStringWithSeconds
import ru.emkn.kotlin.sms.finishprotocol.toStringWithoutHours
import ru.emkn.kotlin.sms.startprotocol.toStringWithSeconds
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import java.time.Duration

internal class LocalTimeTests {

    private val time0 = LocalTime.of(0, 0, 0)
    private val time1 = LocalTime.of(1, 23, 45)
    private val time2 = LocalTime.of(6, 54, 32)
    private val time3 = LocalTime.of(23, 59, 59)
    private val duration0 = Duration.ofSeconds(0)
    private val duration1 = Duration.ofSeconds(5025)
    private val duration2 = Duration.ofSeconds(24872)
    private val duration3 = Duration.ofSeconds(86399)

    @Test
    fun testMinus() {
        assertEquals(time1 - time0, duration1)
        assertEquals(time2 - time0, duration2)
        assertEquals(time3 - time0, duration3)
        assertEquals(time1 - time1, duration0)
        assertEquals(time2 - time2, duration0)
        assertEquals(time3 - time3, duration0)
        assertEquals(time2 - time1, duration2 - duration1)
        assertEquals(time1 - time2, Duration.ofSeconds(86400) + duration1 - duration2)
        assertEquals(time0 - time3, Duration.ofSeconds(86400) + duration0 - duration3)
    }

    @Test
    fun testToStringWithSeconds1() {
        assertEquals(time0.toStringWithSeconds(), "00:00:00")
        assertEquals(time1.toStringWithSeconds(), "01:23:45")
        assertEquals(time2.toStringWithSeconds(), "06:54:32")
        assertEquals(time3.toStringWithSeconds(), "23:59:59")
    }

    @Test
    fun testToStringWithSeconds2() {
        assertEquals(duration0.toStringWithSeconds(), "00:00:00")
        assertEquals(duration1.toStringWithSeconds(), "01:23:45")
        assertEquals(duration2.toStringWithSeconds(), "06:54:32")
        assertEquals(duration3.toStringWithSeconds(), "23:59:59")
    }
    @Test
    fun testToStringWithoutHours() {
        assertEquals(duration0.toStringWithoutHours(), "00:00")
        assertEquals(duration1.toStringWithoutHours(), "1:23:45")
        assertEquals(duration2.toStringWithoutHours(), "6:54:32")
        assertEquals(duration3.toStringWithoutHours(), "23:59:59")
    }

    @Test
    fun testToInt() {
        assertEquals(time0.toInt(), 0)
        assertEquals(time1.toInt(), 5025)
        assertEquals(time2.toInt(), 24872)
        assertEquals(time3.toInt(), 86399)
    }
}