package ru.emkn.kotlin.sms.view

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.dir
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.CompetitionSerialization
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.application.Application
import ru.emkn.kotlin.sms.model.application.TeamApplication
import ru.emkn.kotlin.sms.view.application_view.AplUplWinManager
import ru.emkn.kotlin.sms.view.application_view.ApplicationUploadingWindow
import ru.emkn.kotlin.sms.view.competition_window.CompetitionWindow
import ru.emkn.kotlin.sms.view.competition_window.CompetitionWindowsManager
import java.io.File

enum class Win {
    START, APPLICATION_UPLOADING, COMPETITION, RESULT_UPLOADING;
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
        model.competition = getCompetition(name)
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

    private fun getCompetition(name: String): Competition {
        require(checkCompetitionExist(name))
        val data: List<List<String>>? = getData(name) //получение Competition data
        val info: MetaInfo? = getMetaInfo(name) // получение мета информации
        require(data != null)
        require(info != null)
        return getCompetition(data, info) //создание соревнования
    }

    private fun getCompetition(data: List<List<String>>, info: MetaInfo): Competition =
        Competition(CompetitionSerialization(data, info.toStringList()))

    private fun getData(name: String): List<List<String>>? {
        return try {
            csvReader().readAll(File("$dir$name/competitionData.csv"))
        } catch (e: Exception) {
            null
        }
    }

    private fun getMetaInfo(name: String): MetaInfo? = try {
        MetaInfo(csvReader().readAll(File("$dir$name/competitionInfo.csv"))[0])
    } catch (e: Exception) {
        null
    }

    private fun checkCompetitionExist(name: String): Boolean {
        if (!File("$dir$name/competitionData.csv").exists()) {
            return true
        }
        return false
    }

}