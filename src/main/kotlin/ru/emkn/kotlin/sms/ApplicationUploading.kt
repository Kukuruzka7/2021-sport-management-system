package ru.emkn.kotlin.sms

import ru.emkn.kotlin.sms.model.application.Application
import ru.emkn.kotlin.sms.view.application_view.ApplicationUploadingWindow

class ApplicationUploading {
    fun application(): Application {
        val window = ApplicationUploadingWindow()
        window.render()
        return Application(window.files.distinct())
    }
}