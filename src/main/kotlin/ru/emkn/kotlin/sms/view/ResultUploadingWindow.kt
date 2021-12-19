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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberDialogState
import ru.emkn.kotlin.sms.view.ColorScheme.BACKGROUND_C
import java.awt.FileDialog
import java.io.File

interface ResUplWinManager : WindowManager {
    fun closeResUplWindow()
    fun saveResults(files: List<File>)
}

class ResultUploadingWindow(private val winManager: ResUplWinManager) : IWindow(winManager) {
    private val files: MutableList<File> = mutableListOf()
    private val count = mutableStateOf(files.size)
    private val openingResult = mutableStateOf(-1)
    private val finished = mutableStateOf(false)

    @Composable
    override fun render() {
        Window(
            onCloseRequest = { winManager.closeResUplWindow() },
            title = "Upload results",
            state = WindowState(width = WIDTH, height = HEIGHT)
        ) {
            Box(
                Modifier.background(BACKGROUND_C)
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
                        val fileDialog = FileDialog(ComposeWindow())
                        NewFilesButton(Modifier) {
                            fileDialog.isMultipleMode = true
                            fileDialog.isVisible = true
                            files += fileDialog.files.filter { it.name.endsWith(".csv") }
                            count.value = files.size
                        }
                        SaveButton {
                            finished.value = true
                        }
                    }
                    for (i in 0 until count.value) {
                        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(DEL_SHIFT), Alignment.Top) {
                            FileButton(files[i].name) {
                                openingResult.value = i
                            }
                            DeleteFileButton {
                                files.removeAt(i)
                                count.value--
                            }
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

    companion object {
        val UPPL_GAP = 5.dp
        val DEL_SHIFT = 5.dp
        val MAIN_BUTTONS_GAP = 5.dp
        val WIDTH = 1500.dp
        val HEIGHT = 1000.dp
    }
}

private val BTN_HEIGHT = 50.dp
private val BTN_WIDTH = 50.dp
private val FILE_BTN_WIDTH = 500.dp
private val CORNERS = 4.dp

@Composable
private fun FileButton(name: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = ColorScheme.FOREGROUND_C),
        modifier = Modifier
            .clip(RoundedCornerShape(CORNERS))
            .size(FILE_BTN_WIDTH, BTN_HEIGHT)
            .focusable(interactionSource = interactionSource),
        onClick = onClick
    ) {
        Text(text = name, color = ColorScheme.TEXT_C)
    }
}

@Composable
private fun SaveButton(onClick: () -> Unit) {
    val text = "Сохранить"
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = ColorScheme.FOREGROUND_C),
        onClick = onClick
    ) {
        Text(text, color = ColorScheme.TEXT_C)
    }
}

@Composable
private fun NewFilesButton(modifier: Modifier, onClick: () -> Unit) {
    val text = "Загрузить результаты"
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = ColorScheme.FOREGROUND_C),
        onClick = onClick
    ) {
        Text(text, color = ColorScheme.TEXT_C)
    }
}


@Composable
private fun DeleteFileButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
        onClick = onClick,
    ) { Icon(Icons.Default.Delete, contentDescription = null, tint = ColorScheme.ACCENT_C) }
}