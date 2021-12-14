package ru.emkn.kotlin.sms.view.button

import androidx.compose.ui.unit.Dp

interface IDeleteFileButton : IButton {
    val HEIGHT: Dp
    val WIDTH: Dp
    val text: String
}