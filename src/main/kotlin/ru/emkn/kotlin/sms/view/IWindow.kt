package ru.emkn.kotlin.sms.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

abstract class IWindow(manager: WindowManager) {
    val state: MutableState<Boolean> = mutableStateOf(false)


    @Composable
    abstract fun render()
}
