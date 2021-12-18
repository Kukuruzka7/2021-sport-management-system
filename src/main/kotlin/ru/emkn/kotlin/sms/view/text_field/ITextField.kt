package ru.emkn.kotlin.sms.view.text_field

import androidx.compose.runtime.Composable

interface ITextField {
    val readOnly: Boolean
    @Composable
    fun render()
}