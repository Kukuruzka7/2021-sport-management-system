package ru.emkn.kotlin.sms.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.SportType
import ru.emkn.kotlin.sms.model.application.Application
import ru.emkn.kotlin.sms.view.application_view.ApplicationUploadingWindow
import ru.emkn.kotlin.sms.view.table_view.*
import java.io.File


enum class Win {
    START, APPLICATION_UPLOADING, COMPETITION;
}

interface WindowManager {
}

object View {
    fun render() {
        val compik = Competition(
            MetaInfo("NadeusZarabotaet2", LocalDate(2021, 12, 15), SportType.RUNNING),
            Application(
                listOf(
                    File("src/test/testFiles/testTeamApplication/teamApplication1.csv"),
                    File("src/test/testFiles/testTeamApplication/teamApplication2.csv")
                )
            )
        )
        val manager = Manager(Model(compik))
        manager.create(Win.START)
        application {
            manager.map.values.forEach {
                if (it != null && it.state.value) {
                    it.render()
                }
            }
        }
    }
}