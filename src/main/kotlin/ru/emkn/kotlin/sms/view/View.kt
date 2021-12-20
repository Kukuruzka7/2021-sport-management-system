package ru.emkn.kotlin.sms.view

import androidx.compose.ui.window.application


class View(val model: Model, val manager: Manager) {
    fun render() {
        manager.open(Win.APPLICATION_UPLOADING)
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