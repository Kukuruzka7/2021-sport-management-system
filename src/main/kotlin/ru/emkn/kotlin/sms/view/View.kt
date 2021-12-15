package ru.emkn.kotlin.sms.view

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ru.emkn.kotlin.sms.view.table_view.*

object View {
    fun render() {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Tatarstan Supergut",
                state = rememberWindowState(width = 2000.dp, height = 1080.dp)
            ) {
                WithHeaderMutableTableView(
                    listOf(
                        listOf("Фамилия", "Имя", "Отчество", "Г.р."),
                        listOf("Москаленко", "Тимофей", "Дмитриевич", "2004"),
                        listOf("Миргалимова", "Розалина", "Зуфаровна", "2003"),
                        listOf("Сибгатуллин", "Данил", "Игоревич", "2002"),
                    ),
                    TableType.APPLICATION
                ).render()
            }
        }
    }
}