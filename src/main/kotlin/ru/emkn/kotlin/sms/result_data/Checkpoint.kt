package ru.emkn.kotlin.sms.result_data


import kotlinx.datetime.LocalDateTime
import ru.emkn.kotlin.sms.athlete.AthleteNumber
import java.time.LocalTime


data class Checkpoint(val name: String) {
    override operator fun equals(other: Any?): Boolean {
        if (other !is Checkpoint) {
            return false
        }
        return name == other.name
    }

    override fun toString(): String = name
}

data class CheckpointRes(val checkpoint: Checkpoint, val athleteNumber: AthleteNumber, val date: LocalTime) {
    override fun equals(other: Any?): Boolean {
        if (other !is CheckpointRes) {
            return false;
        }
        return checkpoint == other.checkpoint && athleteNumber == other.athleteNumber && date == other.date
    }
}

