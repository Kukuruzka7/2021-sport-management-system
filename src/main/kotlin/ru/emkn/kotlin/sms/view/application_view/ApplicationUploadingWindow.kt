package ru.emkn.kotlin.sms.view.application_view

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberDialogState
import ru.emkn.kotlin.sms.model.application.TeamApplication
import ru.emkn.kotlin.sms.view.ColorScheme.ACCENT_C
import ru.emkn.kotlin.sms.view.ColorScheme.BACKGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.FOREGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.GREY_C
import ru.emkn.kotlin.sms.view.ColorScheme.SCROLLBAR_HOVER_C
import ru.emkn.kotlin.sms.view.ColorScheme.SCROLLBAR_UNHOVER_C
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C
import ru.emkn.kotlin.sms.view.IWindow
import ru.emkn.kotlin.sms.view.StartWindow
import ru.emkn.kotlin.sms.view.WindowManager
import ru.emkn.kotlin.sms.view.table_view.TableContent
import ru.emkn.kotlin.sms.view.table_view.TableType
import java.awt.FileDialog
import java.io.File

interface AplUplWinManager : WindowManager {
    fun closeAplUplWindow()
    fun saveApplication(files: List<File>)
}

class ApplicationUploadingWindow(private val winManager: AplUplWinManager) : IWindow(winManager) {
    private val competitionName = mutableStateOf("")
    private val competitionDate = mutableStateOf("")
    private val competitionSportType = mutableStateOf("")
    private val files: MutableList<File> = mutableListOf()
    private val count = mutableStateOf(files.size)
    private val openingApplication = mutableStateOf(-1)
    private val finished = mutableStateOf(false)

    @Composable
    override fun render() {
        Window(
            onCloseRequest = { winManager.closeAplUplWindow() },
            title = "Upload team's applications",
            state = WindowState(width = WIDTH, height = HEIGHT)
        ) {
            Box(
                modifier = Modifier.background(BACKGROUND_C).fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                if (openingApplication.value != -1) {
                    openTeamApplication()
                }

                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                    Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.Top) {
                        drawCompetitionDataRow("Название соревнования", competitionName.value) {
                            competitionName.value = it
                        }
                        drawCompetitionDataRow("Дата соревнования", competitionDate.value) {
                            competitionDate.value = it
                        }
                        drawCompetitionDataRow("Тип спорта", competitionSportType.value) {
                            competitionSportType.value = it
                        }
                        val fileDialog = FileDialog(ComposeWindow())
                        NewFilesButton(Modifier.align(Alignment.CenterVertically)) {
                            fileDialog.isMultipleMode = true
                            fileDialog.isVisible = true
                            files += fileDialog.files.filter { it.name.endsWith(".csv") }
                            count.value = files.size
                        }
                        SaveButton(Modifier.align(Alignment.CenterVertically)) {
                            finished.value = true
                        }
                    }
                    repeat(count.value) { i ->
                        Row(Modifier.wrapContentWidth(), Arrangement.spacedBy(DEL_SHIFT), Alignment.Top) {
                            FileButton(files[i].name) {
                                openingApplication.value = i
                            }
                            DeleteFileButton {
                                files.removeAt(i)
                                count.value--
                            }
                        }
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState = scrollState),
                    style = ScrollbarStyle(
                        hoverColor = SCROLLBAR_HOVER_C, unhoverColor = SCROLLBAR_UNHOVER_C,
                        minimalHeight = 16.dp, thickness = 8.dp,
                        shape = RoundedCornerShape(4.dp), hoverDurationMillis = 300,
                    )
                )
            }
        }
    }


    @Composable
    private fun openTeamApplication() {
        val isOpen = mutableStateOf(true)
        val application = TeamApplication(files[openingApplication.value], openingApplication.value)
        val rows = application.rows.drop(2)
        val name = application.teamName.name
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
                    adapter = rememberScrollbarAdapter(scrollState = tableStateHorizontal),
                    style = ScrollbarStyle(
                        hoverColor = SCROLLBAR_HOVER_C, unhoverColor = SCROLLBAR_UNHOVER_C,
                        minimalHeight = 16.dp, thickness = 8.dp,
                        shape = RoundedCornerShape(4.dp), hoverDurationMillis = 300,
                    )
                )
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState = tableStateVertical),
                    style = ScrollbarStyle(
                        hoverColor = SCROLLBAR_HOVER_C, unhoverColor = SCROLLBAR_UNHOVER_C,
                        minimalHeight = 16.dp, thickness = 8.dp,
                        shape = RoundedCornerShape(4.dp), hoverDurationMillis = 300,
                    )
                )
            }
            if (!isOpen.value) {
                openingApplication.value = -1
            }
        }
    }

    companion object {
        val GOR_PAD = 5.dp
        val VER_PAD = 10.dp
        val UPPL_GAP = 5.dp
        val DEL_SHIFT = 5.dp
        val MAIN_BUTTONS_GAP = 5.dp
        val WIDTH = 1000.dp
        val HEIGHT = 850.dp
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
        colors = ButtonDefaults.buttonColors(backgroundColor = FOREGROUND_C),
        modifier = Modifier
            .clip(RoundedCornerShape(CORNERS))
            .size(FILE_BTN_WIDTH, BTN_HEIGHT)
            .focusable(interactionSource = interactionSource),
        onClick = onClick
    ) {
        Text(text = name, color = TEXT_C)
    }
}

@Composable
private fun SaveButton(modifier: Modifier, onClick: () -> Unit) {
    val text = "Сохранить"
    IconButton(
        modifier = modifier,
//        colors = ButtonDefaults.buttonColors(backgroundColor = FOREGROUND_C),
        onClick = onClick
    ) {
        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = ACCENT_C)
//        Text(text, color = TEXT_C)
    }
}

@Composable
private fun NewFilesButton(modifier: Modifier, onClick: () -> Unit) {
    val text = "Загрузить файлы"
    IconButton(
        modifier = modifier,
//        colors = ButtonDefaults.buttonColors(backgroundColor = FOREGROUND_C),
        onClick = onClick
    ) {
        Icon(Icons.Default.Add, contentDescription = null, tint = ACCENT_C)
//        Text(text, color = TEXT_C)
    }
}


@Composable
private fun DeleteFileButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
        onClick = onClick,
    ) { Icon(Icons.Default.Delete, contentDescription = null, tint = ACCENT_C) }
}

var WIDTH_OF_TEXT = 200.dp

@Composable
private fun drawCompetitionDataRow(
    textOfUnit: String,
    text: String,
    onValueChange: (String) -> Unit
) {
    Column(
        Modifier.wrapContentSize(),
        Arrangement.spacedBy(ApplicationUploadingWindow.MAIN_BUTTONS_GAP),
        Alignment.CenterHorizontally
    ) {

        TextField(
            colors = TextFieldDefaults.textFieldColors(
                textColor = TEXT_C,
                focusedIndicatorColor = ACCENT_C,
                cursorColor = ACCENT_C
            ),
            singleLine = true,
            value = text,
            onValueChange = onValueChange,
            readOnly = false,
            placeholder = {
                Text(modifier = Modifier.width(WIDTH_OF_TEXT), text = textOfUnit, color = GREY_C)
            }
        )
    }
}
