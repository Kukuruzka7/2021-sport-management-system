package ru.emkn.kotlin.sms.view

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ru.emkn.kotlin.sms.view.table_view.ColumnInfo
import ru.emkn.kotlin.sms.view.table_view.TableView

object View {
    fun render() {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Tatarstan Supergut",
                state = rememberWindowState(width = 2000.dp, height = 1080.dp)
            ) {
                TableView(
                    listOf(
                        ColumnInfo("Фамилия", 200.dp),
                        ColumnInfo("Имя", 150.dp),
                        ColumnInfo("Отчество", 200.dp),
                        ColumnInfo("Г.р.", 100.dp, true)
                    ),
                    "src/main/resources/tableTest.csv"
                ).draw()
            }
        }
    }
}