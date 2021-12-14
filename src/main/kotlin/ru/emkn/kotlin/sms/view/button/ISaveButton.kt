package ru.emkn.kotlin.sms.view.button

interface ISaveButton : IButton {
    val onClick: () -> Unit
    val text: String
}