package ru.emkn.kotlin.sms.view

import androidx.compose.ui.window.application
import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.SportType
import ru.emkn.kotlin.sms.model.application.Application
import java.io.File


class View(val model: Model, val manager: Manager) {
    fun render() {
        manager.open(Win.START)
        application {
            manager.map.values.forEach {
                if (it != null && it.state.value) {
                    it.render()
                }
            }
            model.save()
        }
    }
}