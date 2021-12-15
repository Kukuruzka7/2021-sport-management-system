package ru.emkn.kotlin.sms.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

abstract class IWindow {
    @Composable
    abstract fun render()
    abstract val state: MutableState<Boolean>
}