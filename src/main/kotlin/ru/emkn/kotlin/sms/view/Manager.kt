package ru.emkn.kotlin.sms.view

import ru.emkn.kotlin.sms.view.application_view.AplUplWinManager
import ru.emkn.kotlin.sms.view.application_view.ApplicationUploadingWindow
import ru.emkn.kotlin.sms.view.competition_window.CompetitionWindow
import ru.emkn.kotlin.sms.view.competition_window.CompetitionWindowsManager
import java.io.File

enum class Win {
    START, APPLICATION_UPLOADING, COMPETITION, RESULT_UPLOADING, EXCEPTION;
}

interface WindowManager

class Manager(val model: IModel) : AplUplWinManager, StartWindowManager, CompetitionWindowsManager, ResUplWinManager, ExceptionWindowManager {
    val map: MutableMap<Win, IWindow?> = Win.values().associateWith { null }.toMutableMap()

    fun create(win: Win) {
        map[win] = when (win) {
            Win.START -> StartWindow(this)
            Win.APPLICATION_UPLOADING -> ApplicationUploadingWindow(this)
            Win.COMPETITION -> CompetitionWindow(model, this)
            Win.RESULT_UPLOADING -> ResultUploadingWindow(this)
            Win.EXCEPTION -> ExceptionWindow(this)
        }
        map[win]?.state?.value = true
    }

    fun close(win: Win) {
        map[win]?.state?.value = false
        map[win] = null
    }

    override fun saveApplication(files: List<File>) {
        TODO("Not yet implemented")
    }

    override fun openAplUplWindow() {
        map[Win.APPLICATION_UPLOADING] = ApplicationUploadingWindow(this)
        map[Win.APPLICATION_UPLOADING]?.state?.value = true
    }

    override fun openCompetitionWindow(name: String) {
        TODO("Not yet implemented")
    }

    override fun closeStartWindow() = close(Win.START)

    override fun closeAplUplWindow() = close(Win.APPLICATION_UPLOADING)

    override fun closeCompWindow() = close(Win.COMPETITION)

    override fun closeResUplWindow() = close(Win.RESULT_UPLOADING)

    override fun closeExceptionWindow() = close(Win.EXCEPTION)

    override fun saveResults(files: List<File>) {
        TODO("Not yet implemented")
    }
}