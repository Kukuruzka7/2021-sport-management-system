package ru.emkn.kotlin.sms.view.button

import androidx.compose.runtime.Composable

interface IButton {
    val text: String
    @Composable
    fun render()
}