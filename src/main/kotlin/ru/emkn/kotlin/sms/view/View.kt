package ru.emkn.kotlin.sms.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ru.emkn.kotlin.sms.view.application_view.ApplicationUploadingWindow
import ru.emkn.kotlin.sms.view.table_view.*

object Controller {
    val windows: List<IWindow> = listOf(StartWindow(),ApplicationUploadingWindow())
}

object View {
    fun render() {
        application {
            Controller.windows.forEach {
                if (it.state.value) {
                    it.render()
                }
            }
        }
    }
}