import org.junit.Test
import ru.emkn.kotlin.sms.athlete.AthleteNumber
import ru.emkn.kotlin.sms.input_result.InputAthleteResults
import ru.emkn.kotlin.sms.input_result.InputCheckpointResults
import ru.emkn.kotlin.sms.result_data.Checkpoint
import ru.emkn.kotlin.sms.result_data.CheckpointRes
import java.time.LocalTime
import kotlin.test.assertEquals

class InputResultTest {
    val inputAthleteResultsNum1 =
        InputAthleteResults(listOf("#1", "Alfa", "12:01:00", "Bravo", "12:02:00", "Charlie", "12:03:00"))
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
    fun testGetCheckPointTest() {
        assertEquals(Checkpoint("Checkpoint Charlie"), inputCheckpointResultsCharlie.checkpoint)
        assertEquals(Checkpoint("пустой"), inputCheckpointResultsEmpty.checkpoint)
        assertEquals(Checkpoint("145"), inputCheckpointResultsSplits.checkpoint)
    }

    @Test
    fun testGetAthleteNumber() {
        assertEquals(AthleteNumber("#1"), inputAthleteResultsNum1.number)
        assertEquals(AthleteNumber("пустой"), inputAthleteResultsEmpty.number)
        assertEquals(AthleteNumber("145"), inputAthleteResultsSplits.number)
    }

    @Test
    fun testResultsOnCheckPoints() {
        assertEquals(
            listOf(
                CheckpointRes(Checkpoint("Alfa"), AthleteNumber("#1"), LocalTime.parse("12:01:00")),
                CheckpointRes(Checkpoint("Bravo"), AthleteNumber("#1"), LocalTime.parse("12:02:00")),
                CheckpointRes(Checkpoint("Charlie"), AthleteNumber("#1"), LocalTime.parse("12:03:00")),
            ),
            inputAthleteResultsNum1.resultsOnCheckPoints
        )
        assertEquals(
            emptyList(),
            inputAthleteResultsEmpty.resultsOnCheckPoints
        )
        assertEquals(
            listOf(
                CheckpointRes(Checkpoint("241"), AthleteNumber("145"), LocalTime.parse("12:48:55")),
                CheckpointRes(Checkpoint("32"), AthleteNumber("145"), LocalTime.parse("12:49:33")),
                CheckpointRes(Checkpoint("46"), AthleteNumber("145"), LocalTime.parse("12:50:42")),
                CheckpointRes(Checkpoint("34"), AthleteNumber("145"), LocalTime.parse("12:52:17")),
                CheckpointRes(Checkpoint("33"), AthleteNumber("145"), LocalTime.parse("12:54:32")),
                CheckpointRes(Checkpoint("53"), AthleteNumber("145"), LocalTime.parse("12:56:08")),
                CheckpointRes(Checkpoint("240"), AthleteNumber("145"), LocalTime.parse("12:56:22")),
            ),
            inputAthleteResultsSplits.resultsOnCheckPoints
        )
    }
}