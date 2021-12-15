package ru.emkn.kotlin.sms.view

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ru.emkn.kotlin.sms.view.table_view.*
import java.io.File

object View {
    fun render() {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Tatarstan Supergut",
                state = rememberWindowState(width = 2000.dp, height = 1080.dp)
            ) {
                WithHeaderTableView(
                    File("src/main/resources/competitions/NadeusZarabotaet/finishProtocol/overallCSV.csv").readLines().drop(2)
                        .map { it.split(",") },
                    TableType.FINISH_PROTOCOL
                ).render()
            }
        }
    }
}