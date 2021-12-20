package ru.emkn.kotlin.sms.view

import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.application.Application
import ru.emkn.kotlin.sms.model.application.TeamApplication
import ru.emkn.kotlin.sms.view.application_view.AplUplWinManager
import ru.emkn.kotlin.sms.view.application_view.ApplicationUploadingWindow
import ru.emkn.kotlin.sms.view.competition_window.CompetitionWindow
import ru.emkn.kotlin.sms.view.competition_window.CompetitionWindowsManager
import java.io.File

enum class Win {
    START, APPLICATION_UPLOADING, COMPETITION, RESULT_UPLOADING, EXCEPTION;
}

interface WindowManager

class Manager(val model: Model) : AplUplWinManager, StartWindowManager, CompetitionWindowsManager, ResUplWinManager {
    val map: MutableMap<Win, IWindow?> = Win.values().associateWith { null }.toMutableMap()


    //Создает новое окно (работает со START, APPLICATION_UPLOADING, COMPETITION, EXCEPTION)
    fun open(win: Win) {
        map[win] = when (win) {
            Win.START -> StartWindow(this)
            Win.APPLICATION_UPLOADING -> ApplicationUploadingWindow(this)
            Win.COMPETITION -> CompetitionWindow(model, this)
            Win.EXCEPTION -> ExceptionWindow(this)
            else -> map[win]
        }
        map[win]?.state?.value = true
    }


    private fun open(win: Win, window: IWindow) {
        map[win] = window
        window.state.value = true
    }

    private fun close(win: Win) {
        val closeWin = map[win]
        if (closeWin != null) {
            closeWin.state.value = false
        }
        map[win] = null
    }

    override fun saveApplication(applications: List<TeamApplication>) {
        model.competitionBuilder.application(Application(applications))
    }

    override fun saveMetaInfo(info: MetaInfo) {
        model.competitionBuilder.info(info)
        model.checkBuilder()
    }

    override fun getCompetitionsNames(): List<String> = model.competitionsNames

    override fun giveCompetitionNameToModel(name: String) {
        model.setCompetition(name)
    }

    override fun addCompetitionName(name: String) {
        model.competitionsNames.add(name)
    }

    override fun openAplUplWindow() {
        map[Win.APPLICATION_UPLOADING] = ApplicationUploadingWindow(this)
        map[Win.APPLICATION_UPLOADING]!!.state.value = true
    }

    override fun openCompetitionWindow(name: String) {
        giveCompetitionNameToModel(name)
        open(Win.COMPETITION)
    }

    override fun openCompetitionWindow() {
        open(Win.COMPETITION)
    }

    override fun closeStartWindow() = close(Win.START)

    override fun closeAplUplWindow() = close(Win.APPLICATION_UPLOADING)



    override fun closeCompWindow() = close(Win.COMPETITION)

    override fun openResUplWindow(resultType: ResultType) {
        open(Win.RESULT_UPLOADING, ResultUploadingWindow(model, this, resultType))
    }

    override fun closeResUplWindow() = close(Win.RESULT_UPLOADING)

    override fun saveResults(files: List<File>) {
        model.stage.value = Model.Companion.Stage.FINISHED
        TODO("Not yet implemented")
    }
}