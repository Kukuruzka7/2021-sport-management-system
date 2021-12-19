package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import java.io.File

interface ExceptionWindowManager : WindowManager {
    fun closeExceptionWindow()
    fun saveResults(files: List<File>)
}

class ExceptionWindow(val winManager: ExceptionWindowManager) : IWindow(winManager) {
    var e: Exception? = null

    @Composable
    override fun render() {
        Dialog(
            onCloseRequest = { winManager.closeExceptionWindow() },
            title = "Ошибка"
        ) {
            Box(modifier = Modifier.background(BACKGROUND_COLOR).fillMaxSize(), contentAlignment = Alignment.Center) {
                Column{
                    Text(
                        e?.message
                            ?: "Во время инициализации программы произошла сурьезная ошибка.\n Либо не хватает памяти, либо не читается диск, либо вы стерли из каталога пару важных файлов.\n Короче говоря не судьба.",
                        color = TEXT_COLOR
                    )
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = BUTTON_COLOR),
                        onClick = { winManager.closeExceptionWindow() }
                    ) {
                        Text("Я все исправлю честно-честно.", color = TEXT_COLOR)
                    }
                }
            }
        }
    }

    companion object {
        val BACKGROUND_COLOR = Color(0xF1111111)
        val TEXT_COLOR = Color(0xF1dddddd)
        var BUTTON_COLOR = Color(0xF1282828)
    }
}