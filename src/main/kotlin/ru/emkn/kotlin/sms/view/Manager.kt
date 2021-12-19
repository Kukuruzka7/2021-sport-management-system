package ru.emkn.kotlin.sms.view

import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.application.Application
import ru.emkn.kotlin.sms.view.application_view.AplUplWinManager
import ru.emkn.kotlin.sms.view.application_view.ApplicationUploadingWindow
import ru.emkn.kotlin.sms.view.competition_window.CompetitionWindow
import ru.emkn.kotlin.sms.view.competition_window.CompetitionWindowsManager
import java.io.File

enum class Win {
    START, APPLICATION_UPLOADING, COMPETITION, RESULT_UPLOADING, EXCEPTION;
}

interface WindowManager

class Manager(val model: Model) : AplUplWinManager, StartWindowManager, CompetitionWindowsManager, ResUplWinManager,
    ExceptionWindowManager {
    val map: MutableMap<Win, IWindow?> = Win.values().associateWith { null }.toMutableMap()


    fun open(win: Win) {
        map[win] = when (win) {
            Win.START -> StartWindow(this)
            Win.APPLICATION_UPLOADING -> ApplicationUploadingWindow(this)
            Win.COMPETITION -> CompetitionWindow(model, this)
            Win.RESULT_UPLOADING -> ResultUploadingWindow(model, this, TODO())
            Win.EXCEPTION -> ExceptionWindow(this)
        }
        map[win]?.state?.value = true
    }

    fun close(win: Win) {
        //require бы написать (ДС)
        map[win]?.state?.value = false
        map[win] = null
    }

    override fun saveApplication(files: List<File>) {
        val application: Application = TODO()
        model.competitionBuilder.application(application)
        model.checkBuilder()
    }

    override fun saveMetaInfo(info: MetaInfo) {
        model.competitionBuilder.info(info)
        model.checkBuilder()
    }

    override fun openAplUplWindow() {
        map[Win.APPLICATION_UPLOADING] = ApplicationUploadingWindow(this)
        map[Win.APPLICATION_UPLOADING]!!.state.value = true
    }

    override fun openCompetitionWindow(name: String) {
        open(Win.COMPETITION)
    }

    override fun openCompetitionNameDialogueField(): String {
        return "Nadeus Zarabotaet"
    }

    override fun closeStartWindow() = close(Win.START)

    override fun closeAplUplWindow() = close(Win.APPLICATION_UPLOADING)

    override fun closeCompWindow() = close(Win.COMPETITION)

    override fun openCSV(fileName: String) {
        TODO("Not yet implemented")
    }

    override fun closeResUplWindow() = close(Win.RESULT_UPLOADING)

    override fun closeExceptionWindow() = close(Win.EXCEPTION)

    override fun saveResults(files: List<File>) {
        model.stage.value = Model.Companion.Stage.FINISHED
        TODO("Not yet implemented")
    }
}