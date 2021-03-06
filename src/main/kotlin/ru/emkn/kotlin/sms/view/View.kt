package ru.emkn.kotlin.sms.view

import androidx.compose.ui.window.application
import ru.emkn.kotlin.sms.Model


class View(val model: Model, val manager: Manager) {
    fun render() {
        manager.open(Win.START)
        application {
            manager.map.values.forEach {
                if (it != null && it.state.value) {
                    it.render()
                }
            }
        }
    }
}