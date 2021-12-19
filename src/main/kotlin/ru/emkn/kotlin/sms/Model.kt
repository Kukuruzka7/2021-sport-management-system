package ru.emkn.kotlin.sms.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.application.Application

class Model(_info: MetaInfo? = null, _application: Application? = null) {
    lateinit var competition: Competition
    val competitionBuilder = CompetitionBuilder()
    var stage: MutableState<Stage> = mutableStateOf(Stage.ONGOING)
    val competitionsNames: MutableList<String> = mutableListOf()

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