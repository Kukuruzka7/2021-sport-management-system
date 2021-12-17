package ru.emkn.kotlin.sms.view.application_view

import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import ru.emkn.kotlin.sms.view.IWindow
import ru.emkn.kotlin.sms.view.WindowManager
import ru.emkn.kotlin.sms.view.button.IButton
import ru.emkn.kotlin.sms.view.button.IDeleteFileButton
import ru.emkn.kotlin.sms.view.button.ISaveButton
import ru.emkn.kotlin.sms.view.table_view.WithHeaderTableView
import java.awt.FileDialog
import java.io.File

val openingApplication = mutableStateOf(-1)

interface AplUplWinManager : WindowManager {
    fun closeAplUplWindow()
    fun saveApplication(files: List<File>)
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
                        NewFileButton(FileDialog(ComposeWindow())).render()
                        SaveButton {
                            finished.value = true
                        }.render()
                    }
                    for (i in 0 until count.value) {
                        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(DEL_SHIFT), Alignment.Top) {
                            FileButton(files[i], i, openingApplication).render()
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
        //должен быть нормальный код, но пока все зависит от tableView
        //это работает

        val table = WithHeaderMutableTableView(
//            listOf(
//                listOf("Фамилия", "Имя", "Отчество", "Г.р."),
//            ),
            TableType.APPLICATION
        )
        Dialog(
            onCloseRequest = { openingApplication.value = -1 },
            title = "Tatarstan Supergut",
            state = rememberDialogState(
                width = StartWindow.WIDTH,
                height = StartWindow.HEIGHT
            ),

        ) {
            Box(Modifier.fillMaxSize().background(StartWindow.BACKGROUND_COLOR), Alignment.CenterEnd ) {
                val listState = rememberLazyListState()
                val tableCells = MutableList(1) { MutableList(applicationFirstRow.size) { mutableStateOf("") } }
                TableContent(
                    modifier = Modifier.wrapContentSize().align(Alignment.Center),
                    mutable = true,
                    drawHeader = true,
                    firstRow = applicationFirstRow.map { it.toColumnType().getInfo(it) },
                    tableCells
                )
//                HorizontalScrollbar(
//                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxHeight(),
//                    adapter = rememberScrollbarAdapter(scrollState = listState)
//                )
//                VerticalScrollbar(
//                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
//                    adapter = rememberScrollbarAdapter(scrollState = listState)
//                )
            }
            if (!table.isOpen.value) {
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

    private class DeleteFileButton(private val onClick: () -> Unit) : IDeleteFileButton {

        override val HEIGHT = 50.dp
        override val WIDTH = 50.dp

        @Composable
        private fun drawIcon() = Icon(Icons.Default.Delete, contentDescription = null)

        @Composable
        override fun render() {
            IconButton(
                modifier = Modifier.height(HEIGHT).width(WIDTH),
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
            get() = "Сохранить"

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
        val text: String = "Загрузить файлы"

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
                Text(text)
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
        val TEXT_COLOR = Color(0xF1dddddd)
        var BUTTON_COLOR = Color(0xF1282828)
        val ICON_COLOR = Color(0xF12A7BF6)
    }
}
