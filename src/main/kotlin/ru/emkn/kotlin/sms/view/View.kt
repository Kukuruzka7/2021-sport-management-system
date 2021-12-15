package ru.emkn.kotlin.sms.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ru.emkn.kotlin.sms.view.application_view.ApplicationUploadingWindow
import ru.emkn.kotlin.sms.view.table_view.*
import java.io.File


enum class Win {
    START, APPLICATION_UPLOADING;
}

interface WindowManager {
}

object View {
    fun render() {
        val manager = Manager()
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