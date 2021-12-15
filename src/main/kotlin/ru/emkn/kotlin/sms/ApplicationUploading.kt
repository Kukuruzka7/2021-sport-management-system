package ru.emkn.kotlin.sms

import androidx.compose.runtime.Composable
import ru.emkn.kotlin.sms.model.application.Application
import ru.emkn.kotlin.sms.view.application_view.ApplicationUploadingWindow

class ApplicationUploading {
    @Composable
    fun application(): Application {
        val window = ApplicationUploadingWindow()
        window.render()
        return Application(window.files.distinct())
    }
}