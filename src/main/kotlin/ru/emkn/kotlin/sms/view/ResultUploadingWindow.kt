package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberDialogState
import ru.emkn.kotlin.sms.view.button.IButton
import ru.emkn.kotlin.sms.view.button.IDeleteFileButton
import ru.emkn.kotlin.sms.view.button.ISaveButton
import java.awt.FileDialog
import java.io.File

interface ResUplWinManager : WindowManager {
    fun closeResUplWindow()
    fun saveResults(files: List<File>)
}

class ResultUploadingWindow(val winManager: ResUplWinManager) : IWindow(winManager) {
    val files: MutableList<File> = mutableListOf()
    private val count = mutableStateOf(files.size)
    private val openingResult = mutableStateOf(-1)
    var finished = false

    @Composable
    override fun render() {
        Window(
            onCloseRequest = { winManager.closeResUplWindow() },
            title = "Upload results",
            state = WindowState(width = WIDTH, height = HEIGHT)
        ) {
            Box(
                Modifier.background(BACKGROUND_COLOR)
            ) {
                if (openingResult.value != -1) {
                    openResult()
                }

                val scrollState = rememberScrollState()

                // Smooth scroll to specified pixels on first composition
                LaunchedEffect(Unit) { scrollState.animateScrollTo(10000) }

                Column(
                    Modifier.fillMaxSize().verticalScroll(scrollState), Arrangement.spacedBy(UPPL_GAP)
                ) {
                    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(MAIN_BUTTONS_GAP), Alignment.Top) {
                        NewFileButton(FileDialog(ComposeWindow())).render()
                        SaveButton {
                            finished = true
                        }.render()
                    }
                    for (i in 0 until count.value) {
                        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(DEL_SHIFT), Alignment.Top) {
                            FileButton(files[i], i, openingResult).render()
                            DeleteFileButton {
                                files.removeAt(i)
                                count.value--
                            }.render()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun openResult() {
        Dialog(
            onCloseRequest = { openingResult.value = -1 },
            title = "Tatarstan Supergut",
            state = rememberDialogState(width = 2000.dp, height = 1080.dp)
        ) {
//            table.render()
//            if (!table.isOpen.value) {
//                openingResult.value = -1
//            }
        }
    }

    private class FileButton(file: File, val numberOfResultFile: Int, val state: MutableState<Int>) : IButton {
        var WIDTH = 500.dp
        var HEIGHT = 50.dp
        var CORNERS = 4.dp
        val text: String = file.name

        @Composable
        override fun render() {
            val interactionSource = remember { MutableInteractionSource() }
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = BUTTON_COLOR),
                modifier = Modifier
                    .clip(RoundedCornerShape(CORNERS))
                    .size(WIDTH, HEIGHT)
                    .focusable(interactionSource = interactionSource),
                onClick = {
                    state.value = numberOfResultFile
                }) {
                Text(text = text, color = TEXT_COLOR)
            }
        }
    }

    private class DeleteFileButton(private val onClick: () -> Unit) : IDeleteFileButton {

        override val HEIGHT = 50.dp
        override val WIDTH = 50.dp


        @Composable
        override fun render() {
            IconButton(
                modifier = Modifier,
                onClick = onClick,
            ) {
                Icon(
                    Icons.Filled.Delete,
                    modifier = Modifier.size(40.dp),
                    contentDescription = "кнопочка",
                    tint = ICON_COLOR
                )
            }
        }
    }

    private class SaveButton(override val onClick: () -> Unit) : ISaveButton {
        override val text: String
            get() = "Сохранить файлы с результатами"

        @Composable
        override fun render() {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = BUTTON_COLOR),
                onClick = onClick
            ) {
                Text(text, color = TEXT_COLOR)
            }
        }
    }

    private inner class NewFileButton(val fileDialog: FileDialog) : IButton {
        val text: String = "Загрузить результаты"

        @Composable
        override fun render() {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = BUTTON_COLOR),
                onClick = {
                    fileDialog.isMultipleMode = true
                    fileDialog.isVisible = true
                    files += fileDialog.files
                    count.value = files.size
                }
            ) {
                Text(text, color = TEXT_COLOR)
            }
        }

    }

    companion object {
        val UPPL_GAP = 5.dp
        val DEL_SHIFT = 5.dp
        val MAIN_BUTTONS_GAP = 5.dp
        val WIDTH = 1500.dp
        val HEIGHT = 1000.dp
        val BACKGROUND_COLOR = Color(0xF1111111)
        val TEXT_COLOR = Color(0xF1dddddd)
        var BUTTON_COLOR = Color(0xF1282828)
        val ICON_COLOR = Color(0xF12A7BF6)
    }
}
