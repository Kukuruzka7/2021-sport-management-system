package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

object View {
    fun render() {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Compose for Desktop",
                state = rememberWindowState(width = 1280.dp, height = 1080.dp)
            ) {
                val rows = MutableList(0) { MutableList(0) { mutableStateOf("") } }
                val count = remember { mutableStateOf(rows.size) }
                Table(rows, count).draw()
            }
        }
    }
}