package ru.emkn.kotlin.sms.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ru.emkn.kotlin.sms.view.application_view.AplUplWinManager
import ru.emkn.kotlin.sms.view.application_view.ApplicationUploadingWindow
import java.io.File


class Manager : AplUplWinManager, StartWindowManager {
    val map: MutableMap<Win, IWindow?> = Win.values().associateWith { null }.toMutableMap()

    fun create(win: Win) {
        map[win] = when (win) {
            Win.START -> StartWindow(this)
            Win.APPLICATION_UPLOADING -> ApplicationUploadingWindow(this)
        }
        map[win]?.state?.value = true
    }

    fun close(win: Win) {
        map[win]?.state?.value = false
        map[win] = null
    }

    override fun saveApplication(files: List<File>) {
        TODO("Not yet implemented")
    }

    override fun openAplUplWindow() {
        map[Win.APPLICATION_UPLOADING] = ApplicationUploadingWindow(this)
        map[Win.APPLICATION_UPLOADING]?.state?.value = true
    }

    override fun closeStartWindow() = close(Win.START)

    override fun closeAplUplWindow() = close(Win.APPLICATION_UPLOADING)
}