import org.junit.Test
import ru.emkn.kotlin.sms.InvalidDateFormat
import ru.emkn.kotlin.sms.InvalidSportType
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.MetaInfoTooFewArguments

internal class MetaInfoTests {

    @Test
    fun testMetaInfo() {
        val args1 = listOf("Olympiad", "2020-10-10", "running")
        val args2 = listOf("Olympiad", "2020-10-10", "буг")
        val args3 = listOf("Olympiad", "2020", "бег")
        val args4 = listOf("Olympiad")
        val info1 = MetaInfo(args1)
        assert(info1.toString() == "[Olympiad, 2020-10-10, RUNNING]")
        try {
            MetaInfo.check(args2)
        }catch (e: Exception){
            assert(e is InvalidSportType)
        }
        try {
            MetaInfo.check(args3)
        }catch (e: Exception){
            assert(e is InvalidDateFormat)
        }
        try {
            MetaInfo.check(args4)
        }catch (e: Exception){
            assert(e is MetaInfoTooFewArguments)
        }
    }
}
