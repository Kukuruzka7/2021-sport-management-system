package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import ru.emkn.kotlin.sms.view.ColorScheme.ACCENT_C
import ru.emkn.kotlin.sms.view.ColorScheme.BACKGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.FOREGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C
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
            Box(modifier = Modifier.background(BACKGROUND_C).fillMaxSize(), Alignment.Center) {
                Column(
                    modifier = Modifier.fillMaxSize(0.90f),
                    Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        e?.message
                            ?: "Во время инициализации программы произошла сурьезная ошибка.\n Либо не хватает памяти, либо не читается диск, либо вы стерли из каталога пару важных файлов.\n Короче говоря не судьба.",
                        color = TEXT_C
                    )
                    AgreeButton(Modifier) { winManager.closeExceptionWindow() }
                }
            }
        }
    }

    @Composable
    private fun AgreeButton(modifier: Modifier, onClick: () -> Unit) {
        val text = "Я все исправлю честно-честно"
        Button(
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(backgroundColor = ACCENT_C),
            onClick = onClick
        ) {
            Text(text, color = TEXT_C)
        }
    }
}