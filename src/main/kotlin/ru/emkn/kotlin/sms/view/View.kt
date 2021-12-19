package ru.emkn.kotlin.sms.view

import androidx.compose.ui.window.application
import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.SportType
import ru.emkn.kotlin.sms.model.application.Application
import java.io.File


object View {
    fun render() {
        val info = MetaInfo("NadeusZarabotaet2", LocalDate(2021, 12, 15), SportType.RUNNING)
        val application = Application(
            listOf(
                File("src/test/testFiles/testTeamApplication/teamApplication1.csv"),
                File("src/test/testFiles/testTeamApplication/teamApplication2.csv"),
                File("src/test/testFiles/NadeusZarabotaet/Application3.csv")
            )
        )
        val manager = Manager(Model(info, application))
        manager.open(Win.APPLICATION_UPLOADING)
        application {
            manager.map.values.forEach {
                if (it != null && it.state.value) {
                    it.render()
                }
            }
        }
    }
}