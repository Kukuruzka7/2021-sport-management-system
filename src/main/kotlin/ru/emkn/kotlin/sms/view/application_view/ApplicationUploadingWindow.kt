package ru.emkn.kotlin.sms.view.application_view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberDialogState
import kotlinx.datetime.toLocalDate
import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.SportType
import ru.emkn.kotlin.sms.model.application.TeamApplication
import ru.emkn.kotlin.sms.view.*
import ru.emkn.kotlin.sms.view.ColorScheme.ACCENT_C
import ru.emkn.kotlin.sms.view.ColorScheme.BACKGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.FOREGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.GREY_C
import ru.emkn.kotlin.sms.view.ColorScheme.SCROLLBAR_HOVER_C
import ru.emkn.kotlin.sms.view.ColorScheme.SCROLLBAR_UNHOVER_C
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C
import ru.emkn.kotlin.sms.view.table_view.TableContent
import ru.emkn.kotlin.sms.view.table_view.TableType
import ru.emkn.kotlin.sms.view.table_view.toMListMListStr
import java.awt.FileDialog
import java.io.File
import java.util.*

interface AplUplWinManager : WindowManager {
    fun closeAplUplWindow()
    fun saveApplication(files: List<File>)
    fun saveMetaInfo(info: MetaInfo)
    fun getCompetitionsNames(): List<String>
    fun addCompetitionName(name: String)
}

class ApplicationUploadingWindow(private val winManager: AplUplWinManager) : IWindow(winManager) {
    private val competitionName = mutableStateOf("")
    private val competitionDate = mutableStateOf("")
    private val competitionSportType = mutableStateOf("Выбрать тип")

    private val competitionIsDone = mutableStateOf(false)

    private val teamApplications: MutableList<TeamApplication> = mutableListOf()
    private val teamApplicationsNames: MutableList<String> = mutableListOf()
    private val count = mutableStateOf(teamApplications.size)
    private val expanded = mutableStateOf(false)
    private val openingApplication = mutableStateOf(-1)
    private val openingException = mutableStateOf<Exception?>(null)
    private val finished = mutableStateOf(false)
    private val eWindow = ExceptionWindow(winManager)
    private val textColor = mutableStateOf(GREY_C)

    @Composable
    override fun render() {
        Window(
            onCloseRequest = { winManager.closeAplUplWindow() },
            title = "Загрузка командных заявок",
            state = WindowState(width = WIDTH, height = HEIGHT)
        ) {

            Box(
                Modifier.background(BACKGROUND_C).padding(15.dp)
            ) {
                if (openingException.value != null) {
                    eWindow.e = openingException.value
                    eWindow.render()
                    if (eWindow.finished.value) {
                        openingException.value = null
                    }
                }

                if (openingApplication.value != -1) {
                    openTeamApplication()
                }

                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                    Arrangement.spacedBy(DEL_SHIFT),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(modifier = Modifier.wrapContentWidth(), Arrangement.spacedBy(DEL_SHIFT), Alignment.Top) {
                        drawCompetitionDataRow(
                            Modifier.size(HEADER_FIELD_WIDTH, BTN_HEIGHT),
                            "Название соревнования",
                            competitionName.value
                        ) {
                            competitionName.value = it
                        }
                        drawCompetitionDataRow(
                            Modifier.size(HEADER_FIELD_WIDTH, BTN_HEIGHT),
                            "Дата соревнования",
                            competitionDate.value
                        ) {
                            competitionDate.value = it
                        }
                        RaceSelector(modifier = Modifier)
                        val fileDialog = FileDialog(ComposeWindow())
                        CreateFileButton(Modifier.align(Alignment.CenterVertically)) {
                            if (competitionIsDone.value) {
                                teamApplications += TeamApplication("", emptyList(), 1)
                                teamApplicationsNames += ""
                                count.value = teamApplications.size
                            }
                        }
                        DownloadFilesButton(Modifier.align(Alignment.CenterVertically)) {
                            if (competitionIsDone.value) {
                                fileDialog.isMultipleMode = true
                                fileDialog.isVisible = true
                                teamApplications += getTeamApplicationsFromUser(fileDialog) ?: listOf()
                                count.value = teamApplications.size
                            }
                        }
                        SaveButton(Modifier.align(Alignment.CenterVertically)) {
                            if(!competitionIsDone.value) {
                                val e = checkCompetitionData()
                                if (e == null) {
                                    winManager.addCompetitionName(competitionName.value)
                                    competitionIsDone.value = true
                                    winManager.saveMetaInfo(TODO())
                                } else {
                                    eWindow.finished.value = false
                                    openingException.value = e
                                }
                            } else {
                                TODO("После того, как всё заработало" )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(0.90f),
                        Arrangement.spacedBy(DEL_SHIFT),
                        Alignment.CenterHorizontally
                    ) {
                        repeat(count.value) { i ->
                            Row(Modifier.wrapContentWidth(), Arrangement.spacedBy(DEL_SHIFT), Alignment.Top) {
                                val teamN = mutableStateOf(teamApplications[i].teamName)
                                FileField(teamN, Modifier.height(BTN_HEIGHT).width(FILE_FIELD_WIDTH)) {
                                    teamN.value = it
                                    teamApplications[i].teamName = it
                                }
                                ApplicationButton(Modifier.height(BTN_HEIGHT).width(BTN_WIDTH)) {
                                    openingApplication.value = i
                                }
                                DeleteApplicationButton {
                                    teamApplications.removeAt(i)
                                    teamApplicationsNames.removeAt(i)
                                    count.value--
                                }
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

    private fun getTeamApplicationsFromUser(fileDialog: FileDialog): List<TeamApplication>? {
        try {
            val result = fileDialog.files
                .mapIndexed { idx, it -> TeamApplication(it, idx + count.value) }
                .filter { !teamApplicationsNames.contains(it.teamName) }
            teamApplicationsNames += result.map { it.teamName }
            return result
        } catch (e: Exception) {
            eWindow.finished.value = false
            openingException.value = e
        }
        return null
    }


    private fun checkCompetitionData(): Exception? {
        if (SportType.getSportType(competitionSportType.value) == SportType.X) {
            return InvalidSportType(competitionSportType.value)
        }

        try {
            competitionDate.value.toLocalDate()
        } catch (e: Exception) {
            return InvalidDateFormat(competitionDate.value)
        }

        if (competitionName.value == "") {
            return InvalidCompetitionName(competitionName.value)
        }

        if (winManager.getCompetitionsNames().contains(competitionName.value)) {
            return CompetitionAlreadyExist(competitionName.value)
        }

        return null
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
                    openingApplication.value = -1
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

    private companion object {
        val DEL_SHIFT = 15.dp
        val WIDTH = 1000.dp
        val HEIGHT = 850.dp

        val BTN_HEIGHT = 50.dp
        val BTN_WIDTH = 50.dp
        val HEADER_FIELD_WIDTH = 250.dp
        val FILE_FIELD_WIDTH = 400.dp
        val WIDTH_OF_TEXT = 200.dp
    }

    @Composable
    private fun FileField(name: MutableState<String>, modifier: Modifier, onValueChange: (String) -> Unit) {
        TextField(
            modifier = modifier,
            singleLine = true,
            value = name.value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.textFieldColors(
                textColor = TEXT_C,
                focusedIndicatorColor = ACCENT_C,
                cursorColor = ACCENT_C
            ),
            textStyle = TextStyle(fontSize = 17.sp)
        )
    }

    @Composable //Сохраняем всё
    private fun SaveButton(modifier: Modifier, onClick: () -> Unit) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = ACCENT_C)
        }
    }

    @Composable //Кнопка скачивания файлов
    private fun DownloadFilesButton(modifier: Modifier, onClick: () -> Unit) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint =
            if (competitionIsDone.value) {
                ACCENT_C
            } else {
                GREY_C
            })
        }
    }

    @Composable //Кнопка создания пустого заявления
    private fun CreateFileButton(modifier: Modifier, onClick: () -> Unit) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = if (competitionIsDone.value) {
                    ACCENT_C
                } else {
                    GREY_C
                }
            )
        }
    }

    @Composable //Кнопка изменения таблички
    private fun ApplicationButton(modifier: Modifier, onClick: () -> Unit) {
        IconButton(
            modifier = modifier,
            onClick = onClick,
        ) { Icon(Icons.Default.Create, contentDescription = null, tint = ACCENT_C) }
    }

    @Composable //Кнопка удаления заявления
    private fun DeleteApplicationButton(onClick: () -> Unit) {
        IconButton(
            modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
            onClick = onClick,
        ) { Icon(Icons.Default.Delete, contentDescription = null, tint = ACCENT_C) }
    }


    @Composable
    private fun RaceSelector(modifier: Modifier) {
        Column {
            TextField(
                modifier = modifier.width(WIDTH_OF_TEXT).height(BTN_HEIGHT),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = TEXT_C,
                    focusedIndicatorColor = ACCENT_C,
                    cursorColor = ACCENT_C
                ),
                singleLine = true,
                value = "",
                onValueChange = { },
                readOnly = true,
                placeholder = {
                    ClickableTexxxt(
                        modifier = Modifier,
                        text = competitionSportType.value.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                        style = SpanStyle(color = textColor.value, fontSize = 17.sp)
                    ) {
                        if (!competitionIsDone.value) {
                            expanded.value = true
                        }
                    }
                }
            )
            DropdownMenu(
                modifier = Modifier.width(WIDTH_OF_TEXT).background(FOREGROUND_C),
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                Divider(color = BACKGROUND_C, thickness = 0.5.dp)
                SportType.values().dropLast(1).forEach {
                    DropdownMenuItem(onClick = {
                        competitionSportType.value = it.toRussian()
                        textColor.value = TEXT_C
                        expanded.value = false
                    }) {
                        Text(
                            it.toRussian()
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                            color = TEXT_C
                        )
                    }
                    Divider(color = BACKGROUND_C, thickness = 0.5.dp)
                }
            }
        }
    }

    @Composable
    private fun drawCompetitionDataRow(
        modifier: Modifier,
        textOfUnit: String,
        text: String,
        onValueChange: (String) -> Unit
    ) {
        TextField(
            modifier = modifier,
            colors = TextFieldDefaults.textFieldColors(
                textColor = TEXT_C,
                focusedIndicatorColor = ACCENT_C,
                cursorColor = ACCENT_C
            ),
            singleLine = true,
            value = text,
            onValueChange = onValueChange,
            readOnly = competitionIsDone.value,
            placeholder = {
                Text(modifier = Modifier.width(WIDTH_OF_TEXT), text = textOfUnit, color = GREY_C)
            }
        )
    }
}