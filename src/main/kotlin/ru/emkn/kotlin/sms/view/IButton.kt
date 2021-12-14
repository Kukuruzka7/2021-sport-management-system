package ru.emkn.kotlin.sms.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi

interface IButton {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun render()
}