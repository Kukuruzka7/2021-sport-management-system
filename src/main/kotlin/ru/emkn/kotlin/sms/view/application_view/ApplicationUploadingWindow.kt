package ru.emkn.kotlin.sms.view.application_view

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
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
import ru.emkn.kotlin.sms.model.MetaInfo
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
import ru.emkn.kotlin.sms.view.table_view.toMListMListStr
import java.awt.FileDialog
import java.io.File

interface AplUplWinManager : WindowManager {
    fun closeAplUplWindow()
    fun saveApplication(files: List<File>)
    fun saveMetaInfo(info: MetaInfo)
}

class ApplicationUploadingWindow(private val winManager: AplUplWinManager) : IWindow(winManager) {
    private val competitionName = mutableStateOf("")
    private val competitionDate = mutableStateOf("")
    private val competitionSportType = mutableStateOf("")
    private val teamApplications: MutableList<TeamApplication> = mutableListOf()
    private val count = mutableStateOf(teamApplications.size)
    private val openingApplication = mutableStateOf(-1)
    var finished = mutableStateOf(false)

    @Composable
    override fun render() {
        Window(
            onCloseRequest = { winManager.closeAplUplWindow() },
            title = "Загрузка командных заявок",
            state = WindowState(width = WIDTH, height = HEIGHT)
        ) {
            Box(
                Modifier.background(BACKGROUND_C)
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
                            teamApplications += fileDialog.files.filter { it.name.endsWith(".csv") }
                                .mapIndexed { idx, it -> TeamApplication(it, idx + count.value) }
                            count.value = teamApplications.size
                        }
                        SaveButton(Modifier.align(Alignment.CenterVertically)) {
                            finished.value = true
                        }
                    }
                    repeat(count.value) { i ->
                        Row(Modifier.wrapContentWidth(), Arrangement.spacedBy(DEL_SHIFT), Alignment.Top) {
                            FileButton(teamApplications[i].teamName) {
                                openingApplication.value = i
                            }
                            DeleteFileButton {
                                teamApplications.removeAt(i)
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
        val application = teamApplications[openingApplication.value]
        val rows = application.rows
        val name = application.teamName
        val tableCells = rows.toMListMListStr()
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
                    contentRows = tableCells
                ) {
                    teamApplications[openingApplication.value] = TeamApplication(application, tableCells)
                }
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
private val WIDTH_OF_TEXT = 200.dp

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
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = ACCENT_C)
    }
}

@Composable
private fun NewFilesButton(modifier: Modifier, onClick: () -> Unit) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(Icons.Default.Add, contentDescription = null, tint = ACCENT_C)
    }
}


@Composable
private fun DeleteFileButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
        onClick = onClick,
    ) { Icon(Icons.Default.Delete, contentDescription = null, tint = ACCENT_C) }
}

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
