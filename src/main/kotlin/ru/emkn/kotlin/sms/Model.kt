package ru.emkn.kotlin.sms.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.application.Application
import java.io.File

class Model(_info: MetaInfo? = null, _application: Application? = null) {
    var competition: Competition? = null
    val competitionBuilder = CompetitionBuilder()
    var stage: MutableState<Stage> = mutableStateOf(Stage.ONGOING)
    val competitionsNames: MutableList<String> = getAlreadyExistsCompetitions()

    private fun getAlreadyExistsCompetitions(): MutableList<String> =
        csvReader().readAll(File("src/main/resources/competitions/competitionsNames.csv")).map { it[0] }.toMutableList()

    init {
        competitionBuilder.info(_info).application(_application)
        checkBuilder()
    }

    fun checkBuilder() {
        if (competitionBuilder.isReady()) {
            competition = competitionBuilder.build()
        }
    }

    companion object {
        enum class Stage {
            ONGOING, FINISHED;
        }
    }

    fun getFinishProtocolByGroupName(name: String): String {
        require(competition != null)
        return "src/main/resources/competitions/${competition!!.info.name}/finishProtocol/groups/$name.csv"
    }

    fun getStartProtocolByGroupName(name: String): String {
        require(competition != null)
        return "src/main/resources/competitions/${competition!!.info.name}/startProtocol/$name.csv"
    }

    fun saveCompetitionsNames() {
        csvWriter().writeAll(
            List(competitionsNames.size) { listOf(competitionsNames[it]) },
            File("src/main/resources/competitions/competitionsNames.csv")
        )
    }

    fun save() {
        saveCompetitionsNames()
    }
}

class CompetitionBuilder {
    private var info: MetaInfo? = null
    private var application: Application? = null

    fun build(): Competition {
        if (info == null) {
            throw CompetitionIsNotReadyException("info")
        }
        if (application == null) {
            throw CompetitionIsNotReadyException("application")
        }
        return Competition(info!!, application!!)
    }

    fun info(_info: MetaInfo?) = apply { info = _info }

    fun application(_application: Application?) = apply { application = _application }

    fun isReady(): Boolean = info != null && application != null

    class CompetitionIsNotReadyException(property: String) :
        Exception("Property $property has not been initialized yet")

}