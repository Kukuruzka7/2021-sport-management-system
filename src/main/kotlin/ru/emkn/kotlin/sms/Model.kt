package ru.emkn.kotlin.sms

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import logger
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.CompetitionSerialization
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.application.Application
import ru.emkn.kotlin.sms.model.finishprotocol.FinishProtocol
import ru.emkn.kotlin.sms.model.input_result.InputCompetitionResult
import ru.emkn.kotlin.sms.model.result_data.ResultData
import ru.emkn.kotlin.sms.model.startprotocol.StartProtocol
import java.io.File

class Model(_info: MetaInfo? = null, _application: Application? = null) {
    lateinit var competition: Competition
        private set
    val competitionBuilder = CompetitionBuilder()
    var stage: MutableState<Stage> = mutableStateOf(getStage())

    val resourcesPath = "src/main/resources/competitions"

    val competitionPath: String
        get() {
            require(isCompetitionInitialized())
            return "$resourcesPath/${competition.info.name}"
        }

    val competitionsNames = CompetitionNames(resourcesPath)


    init {
        competitionBuilder.info(_info).application(_application)
        checkBuilder()
    }

    fun checkBuilder() {
        if (competitionBuilder.isReady()) {
            competition = competitionBuilder.build()
        }
    }

    fun getStage(): Stage {
        if (!isCompetitionInitialized()) {
            return Stage.ONGOING
        }
        return if (File("$resourcesPath/${competition.info.name}/finishProtocol").exists()) {
            Stage.FINISHED
        } else {
            Stage.ONGOING
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
        return "$competitionPath/startProtocol/$name.csv"
    }

    fun createStartProtocols() {
        require(isCompetitionInitialized())
        StartProtocol(competition.groupList, "$competitionPath/")
    }

    fun saveCompetition(): Boolean {
        if (competitionBuilder.isReady()) {
            competition = competitionBuilder.build()
            stage.value = getStage()
            return true
        }
        return false
    }

    fun saveCompetitionByName(name: String) {
        competition = competitionBuilder.byName(name, resourcesPath)
        stage.value = getStage()
    }

    fun saveResults(result: InputCompetitionResult) {
        require(isCompetitionInitialized())
        stage.value = Stage.FINISHED
        val map = competition.athleteList.associateBy({ it.number }, { it.startTime })
        logger.trace { "Сохраняю результаты ${competition.info.name}" }
        competition.athleteList.forEach {
            logger.info { "${it.number}, ${it.name}" }
        }

        FinishProtocol(ResultData(result, map), competition)
    }


    fun isCompetitionInitialized(): Boolean = this::competition.isInitialized

    class CompetitionNames(path: String) {
        private val file = File("$path/competitionsNames.csv")

        fun add(name: String) {
            if (get().isEmpty()) {
                file.appendText(name)
            } else {
                file.appendText(",$name")

            }
        }

        fun get(): List<String> {
            val rows = csvReader().readAll(file)
            if (rows.isEmpty()) {
                throw return emptyList()
            }
            return rows[0]
        }

        //class CompetitionNamesRepositoryIsDamaged() : Exception("Репозиторий имен соревнований поврежден")
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

    fun byName(name: String, dir: String): Competition {
        val fullPath = "$dir/$name"
        require(checkCompetitionExist(fullPath))
        val data: List<List<String>>? = getData(fullPath) //получение Competition data
        val info: MetaInfo? = getMetaInfo(fullPath) // получение мета информации
        require(data != null)
        require(info != null)
        return Competition(CompetitionSerialization(data, info.toStringList())) //создание соревнования
    }

    private fun getData(fullPath: String): List<List<String>>? = try {
        csvReader().readAll(File("$fullPath/competitionData.csv"))
    } catch (e: Exception) {
        null
    }


    private fun getMetaInfo(fullPath: String): MetaInfo? = try {
        MetaInfo(csvReader().readAll(File("$fullPath/competitionInfo.csv"))[0])
    } catch (e: Exception) {
        null
    }

    private fun checkCompetitionExist(fullPath: String): Boolean =
        File("$fullPath/competitionData.csv").exists()

    class CompetitionIsNotReadyException(property: String) :
        Exception("Property $property has not been initialized yet")

}