package ru.emkn.kotlin.sms.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.CompetitionNamesRepositoryIsDamaged
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.application.Application
import ru.emkn.kotlin.sms.model.startprotocol.StartProtocol
import java.io.File

class Model(_info: MetaInfo? = null, _application: Application? = null) {
    lateinit var competition: Competition
    val competitionBuilder = CompetitionBuilder()
    var stage: MutableState<Stage> = mutableStateOf(Stage.ONGOING)

    private val resourcesPath = "src/main/resources/competitions"

    private val competitionPath: String
        get() {
            require(!isCompetitionInitialized())
            return "resourcesPath/${competition.info.name}"
        }

    val competitionsNames = CompetitionNames(resourcesPath)


    private fun getAlreadyExistingCompetitions(): MutableList<String> =
        csvReader().readAll(File("$resourcesPath/competitionsNames.csv")).map { it[0] }.toMutableList()

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
        return "$competitionPath/finishProtocol/groups/$name.csv"
    }

    fun getStartProtocolByGroupName(name: String): String {
        return "$competitionPath/${competition.info.name}/startProtocol/$name.csv"
    }


    fun createStartProtocols() {
        require(isCompetitionInitialized())
        StartProtocol(competition.groupList, competitionPath)
    }

    fun isCompetitionInitialized(): Boolean = this::competition.isInitialized

    class CompetitionNames(private val path: String) {
        private val file = File("$path/competitionsNames.csv")

        fun add(name: String) {
            file.appendText(",$name")
        }

        fun get(): List<String> {
            val rows = csvReader().readAll(file)
            if (rows.isEmpty()) {
                throw CompetitionNamesRepositoryIsDamaged()
            }
            return rows[0]
        }
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