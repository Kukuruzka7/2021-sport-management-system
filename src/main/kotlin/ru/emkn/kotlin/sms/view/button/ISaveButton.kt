package ru.emkn.kotlin.sms.view.button

interface ISaveButton : IButton {
    val onClick: () -> Unit
    override val text: String
}