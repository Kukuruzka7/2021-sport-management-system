package ru.emkn.kotlin.sms.view.application_view

import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import ru.emkn.kotlin.sms.view.button.IButton
import ru.emkn.kotlin.sms.view.button.IDeleteFileButton
import ru.emkn.kotlin.sms.view.button.ISaveButton
import ru.emkn.kotlin.sms.view.IWindow
import java.awt.FileDialog
import java.io.File


class ApplicationUploadingWindow() : IWindow {
    val files: MutableList<File> = mutableListOf()
    private val count = mutableStateOf(files.size)
    var finished = false

    @OptIn(ExperimentalComposeUiApi::class)
    override fun render() {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Upload team's applications",
                state = WindowState(width = WIDTH, height = HEIGHT)
            ) {
                val scrollState = rememberScrollState()

                // Smooth scroll to specified pixels on first composition
                LaunchedEffect(Unit) { scrollState.animateScrollTo(10000) }

                Column(
                    Modifier.fillMaxSize().verticalScroll(scrollState), Arrangement.spacedBy(UPPL_GAP)
                ) {
                    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(MAIN_BUTTONS_GAP), Alignment.Top) {
                        SaveButton {
                            exitApplication()
                            finished = true
                        }.render()
                        NewFileButton(FileDialog(ComposeWindow())).render()
                    }
                    for (i in 0 until count.value) {
                        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(DEL_SHIFT), Alignment.Top) {
                            FileButton(files[i]).render()
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

    private class FileButton(private val file: File) : IButton {
        var WIDTH = 50.dp
        var HEIGHT = 500.dp
        var CORNERS = 4.dp
        val text: String = file.name

        @Composable
        override fun render() {
            val interactionSource = remember { MutableInteractionSource() }
            Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                modifier = Modifier
                    .clip(RoundedCornerShape(CORNERS))
                    .size(HEIGHT, WIDTH)
                    .focusable(interactionSource = interactionSource),
                //onClick = { println(files[index]) }
                onClick = {}
            ) {
                Text(text = file.name, color = Color.White)
            }
        }
    }

    private class DeleteFileButton(private val onClick: () -> Unit) : IDeleteFileButton {

        override val HEIGHT = 50.dp
        override val WIDTH = 50.dp

        override val text: String
            get() = "–"

        @Composable
        override fun render() {
            Button(
                modifier = Modifier.height(HEIGHT).width(WIDTH),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xF11BFF99)),
                shape = CircleShape,
                onClick = onClick
            ) { Text(text) }
        }
    }

    private class SaveButton(override val onClick: () -> Unit) : ISaveButton {
        override val text: String
            get() = "Сохранить заявки"

        @Composable
        override fun render() {
            Button(
                onClick = onClick
            ) {
                Text(text)
            }
        }
    }

    private inner class NewFileButton(val fileDialog: FileDialog) : IButton {
        val text: String = "Загрузить файл"

        @Composable
        override fun render() {
            Button(
                onClick = {
                    fileDialog.isMultipleMode = true
                    fileDialog.isVisible = true
                    files += fileDialog.files
                    count.value = files.size
                }
            ) {
                Text(text)
            }
        }

    }

    companion object {
        val UPPL_GAP = 5.dp
        val DEL_SHIFT = 5.dp
        val MAIN_BUTTONS_GAP = 5.dp
        val WIDTH = 1500.dp
        val HEIGHT = 1000.dp
    }
}