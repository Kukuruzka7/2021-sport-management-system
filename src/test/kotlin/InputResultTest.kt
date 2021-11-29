import org.junit.Test
import ru.emkn.kotlin.sms.athlete.AthleteNumber
import ru.emkn.kotlin.sms.input_result.InputAthleteResults
import ru.emkn.kotlin.sms.input_result.InputCheckpointResults
import ru.emkn.kotlin.sms.result_data.Checkpoint
import kotlin.test.assertEquals

class InputResultTest {
    val inputAthleteResultsNum1 =
        InputAthleteResults(listOf("#1", "Alfa", "12:01:00", "Beta", "12:02:00", "Charlie", "12:03:00"))
    val inputAthleteResultsEmpty = InputAthleteResults(listOf("пустой"))
    val inputAthleteResultsSplits = InputAthleteResults(
        listOf(
            "145",
            "241", "12:48:55", "32", "12:49:33", "46", "12:50:42",
            "34", "12:52:17", "33", "12:54:32", "53", "12:56:08", "240", "12:56:22"
        )
    )

    val inputCheckpointResultsCharlie = InputCheckpointResults(
        listOf("Checkpoint Charlie", "#1", "12:01:00", "#2", "12:02:00", "#3", "12:03:00", "", "")
    )
    val inputCheckpointResultsEmpty = InputCheckpointResults(listOf("пустой"))
    val inputCheckpointResultsSplits = InputCheckpointResults(
        listOf(
            "145",
            "241", "12:48:55", "32", "12:49:33", "46", "12:50:42",
            "34", "12:52:17", "33", "12:54:32", "53", "12:56:08", "240", "12:56:22"
        )
    )


    @Test
    fun testGetCheckPoint() {
        assertEquals(Checkpoint("Checkpoint Charlie"), inputCheckpointResultsCharlie.checkpoint)
        assertEquals(Checkpoint("пустой"), inputCheckpointResultsEmpty.checkpoint)
        assertEquals(Checkpoint("145"), inputCheckpointResultsSplits.checkpoint)
    }

    @Test
    fun getAthleteNumber() {
        assertEquals(AthleteNumber("#1"), inputAthleteResultsNum1.number)
        assertEquals(AthleteNumber("пустой"), inputAthleteResultsEmpty.number)
        assertEquals(AthleteNumber("145"), inputAthleteResultsSplits.number)
    }

}