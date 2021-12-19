package ru.emkn.kotlin.sms.view.application_view

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.application.TeamApplication
import ru.emkn.kotlin.sms.view.ColorScheme.BACKGROUND_C
import ru.emkn.kotlin.sms.view.IWindow
import ru.emkn.kotlin.sms.view.StartWindow
import ru.emkn.kotlin.sms.view.WindowManager
import ru.emkn.kotlin.sms.view.button.IButton
import ru.emkn.kotlin.sms.view.button.ISaveButton
import ru.emkn.kotlin.sms.view.table_view.*
import java.awt.FileDialog
import java.io.File

val openingApplication = mutableStateOf(-1)

interface AplUplWinManager : WindowManager {
    fun closeAplUplWindow()
    fun saveApplication(files: List<File>)
    fun saveMetaInfo(info: MetaInfo)
}

class ApplicationUploadingWindow(val winManager: AplUplWinManager) : IWindow(winManager) {
    val competitionName = mutableStateOf("")
    val competitionDate = mutableStateOf("")
    val competitionSportType = mutableStateOf("")
    val files: MutableList<File> = mutableListOf()
    private val count = mutableStateOf(files.size)
    private val openingApplication = mutableStateOf(-1)
    var finished = mutableStateOf(false)

    @Composable
    override fun render() {
        Window(
            onCloseRequest = { winManager.closeAplUplWindow() },
            title = "Upload team's applications",
            state = WindowState(width = WIDTH, height = HEIGHT)
        ) {
            Box(
                Modifier.background(BACKGROUND_COLOR)
            ) {
                if (openingApplication.value != -1) {
                    openTeamApplication()
                }

                val scrollState = rememberScrollState()

                // Smooth scroll to specified pixels on first composition
                LaunchedEffect(Unit) { scrollState.animateScrollTo(10000) }

                Column(
                    Modifier.padding(GOR_PAD, VER_PAD).fillMaxSize().verticalScroll(scrollState),
                    Arrangement.spacedBy(UPPL_GAP)
                ) {
                    CompetitionDataButton("Название соревнования: ", competitionName.value) {
                        competitionName.value = it
                    }.render()
                    CompetitionDataButton("Дата соревнования: ", competitionDate.value) {
                        competitionDate.value = it
                    }.render()
                    CompetitionDataButton("Тип спорта: ", competitionSportType.value) {
                        competitionSportType.value = it
                    }.render()
                    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(MAIN_BUTTONS_GAP)) {
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
                            FileButton(files[i], i, openingApplication).render()
                            DeleteFileButton {
                                files.removeAt(i)
                                count.value--
                            }
                        }
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState = scrollState)
                )
            }
        }
    }

    private class CompetitionDataButton(
        val textOfUnit: String,
        val text: String,
        private val onValueChange: (String) -> Unit
    ) {

        @Composable
        fun render() {
            Row(
                Modifier.fillMaxWidth().height(HEIGHT),
                Arrangement.spacedBy(MAIN_BUTTONS_GAP),
                Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.width(WIDTH_OF_TEXT), text = textOfUnit, color = TEXT_COLOR)
                TextField(
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = BUTTON_COLOR,
                        textColor = TEXT_COLOR,
                        focusedIndicatorColor = FOCUS_COLOR,
                        cursorColor = FOCUS_COLOR
                    ),
                    singleLine = true,
                    value = text,
                    onValueChange = onValueChange,
                    readOnly = false
                )
            }
        }

        companion object {
            var WIDTH_OF_TEXT = 200.dp
            var HEIGHT = 50.dp
            var CORNERS = 4.dp
            val FOCUS_COLOR = Color(0xF12A7BF6)
            val CURSOR_COLOR = Color(0xF12A7BF6)
        }
    }


    @Composable
    private fun openTeamApplication() {
        val isOpen = mutableStateOf(true)
        val application = TeamApplication(files[openingApplication.value], openingApplication.value)
        val rows = application.rows.drop(2)
        val name = application.teamName
        val tableCells = rows.map { list -> list.map { mutableStateOf(it) }.toMutableList() }.toMutableList()
        Dialog(
            onCloseRequest = { openingApplication.value = -1 },
            title = name,
            state = rememberDialogState(
                width = StartWindow.WIDTH,
                height = StartWindow.HEIGHT
            ),
        ) {
            Box(Modifier.fillMaxSize().background(BACKGROUND_C), Alignment.CenterEnd) {
                val tableStateVertical = rememberScrollState(0)
                val tableStateHorizontal = rememberScrollState(0)
                TableContent(
                    type = TableType.APPLICATION,
                    modifier = Modifier.wrapContentSize()
                        .align(Alignment.TopCenter)
                        .verticalScroll(tableStateVertical)
                        .horizontalScroll(tableStateHorizontal),
                    open = isOpen,
                    contentRows = tableCells
                )
                HorizontalScrollbar(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                    adapter = rememberScrollbarAdapter(scrollState = tableStateHorizontal)
                )
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState = tableStateVertical)
                )
            }
            if (!isOpen.value) {
                openingApplication.value = -1
            }
        }
    }

    private class FileButton(file: File, val numberOfApplication: Int, val state: MutableState<Int>) : IButton {
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
                    state.value = numberOfApplication
                }) {
                Text(text = text, color = TEXT_COLOR)
            }
        }
    }


    companion object {
        val GOR_PAD = 5.dp
        val VER_PAD = 10.dp
        val UPPL_GAP = 5.dp
        val DEL_SHIFT = 5.dp
        val MAIN_BUTTONS_GAP = 5.dp
        val WIDTH = 1500.dp
        val HEIGHT = 1000.dp
        val BACKGROUND_COLOR = Color(0xF1111111)
    }
}


private val TEXT_COLOR = Color(0xF1dddddd)
private val BUTTON_COLOR = Color(0xF1282828)
private val ICON_COLOR = Color(0xF12A7BF6)
private val BTN_HEIGHT = 50.dp
private val BTN_WIDTH = 50.dp


@Composable
private fun SaveButton(onClick: () -> Unit) {
    val text = "Сохранить"
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = BUTTON_COLOR),
        onClick = onClick
    ) {
        Text(text, color = TEXT_COLOR)
    }
}

@Composable
private fun NewFilesButton(modifier: Modifier, onClick: () -> Unit) {
    val text = "Загрузить файлы"
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = BUTTON_COLOR),
        onClick = onClick
    ) {
        Text(text, color = TEXT_COLOR)
    }
}


@Composable
private fun DeleteFileButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
        onClick = onClick,
    ) { Icon(Icons.Default.Delete, contentDescription = null, tint = ICON_COLOR) }
}
