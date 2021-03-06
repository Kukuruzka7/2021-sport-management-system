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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberDialogState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import ru.emkn.kotlin.sms.CompetitionAlreadyExist
import ru.emkn.kotlin.sms.InvalidCompetitionName
import ru.emkn.kotlin.sms.InvalidDateFormat
import ru.emkn.kotlin.sms.InvalidSportType
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.SportType
import ru.emkn.kotlin.sms.model.application.TeamApplication
import ru.emkn.kotlin.sms.view.ColorScheme.ACCENT_C
import ru.emkn.kotlin.sms.view.ColorScheme.BACKGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.FOREGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.GREY_C
import ru.emkn.kotlin.sms.view.ColorScheme.SCROLLBAR_HOVER_C
import ru.emkn.kotlin.sms.view.ColorScheme.SCROLLBAR_UNHOVER_C
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C
import ru.emkn.kotlin.sms.view.ExceptionWindow
import ru.emkn.kotlin.sms.view.IWindow
import ru.emkn.kotlin.sms.view.StartWindow
import ru.emkn.kotlin.sms.view.WindowManager
import ru.emkn.kotlin.sms.view.table_view.TableContent
import ru.emkn.kotlin.sms.view.table_view.TableType
import ru.emkn.kotlin.sms.view.table_view.toMListMListStr
import java.awt.FileDialog
import java.util.*

interface AplUplWinManager : WindowManager {
    fun closeAplUplWindow()
    fun openCompetitionWindow()
    fun saveApplication(applications: List<TeamApplication>)
    fun saveMetaInfo(info: MetaInfo)
    fun saveCompetition()
    fun getCompetitionsNames(): List<String>
    fun addCompetitionName(name: String)
    fun createStartProtocols()
    fun saveSerialization()
}

//?????????? ???????????????? ?????????????????? ????????????
class ApplicationUploadingWindow(private val winManager: AplUplWinManager) : IWindow(winManager) {
    //???????????????????? ?????? ???????? ???????????? ?????????????? ????????????????????
    private val competitionName = mutableStateOf("")
    private val competitionDate = mutableStateOf("")
    private val competitionSportType = mutableStateOf("?????????????? ??????")

    //???????????????? ???? ???????????? ???????? ???????????? (???????????????? ???????????????? ???????? ?? ???????? ????????????????????????)
    private val metaInfoUploaded = mutableStateOf(false)

    //???????????????????? ?? ??????????????????????????????
    private val teamApplications: MutableList<TeamApplication> = mutableListOf()
    private val teamApplicationsNames: MutableList<String> = mutableListOf()
    private val teamApplicationsSize = mutableStateOf(teamApplications.size)
    private val openingApplication = mutableStateOf(-1)

    //?????????????? ???? ?????????????? ?? ??????????????
    private val menuIsExpanded = mutableStateOf(false)

    private val openingException = mutableStateOf<Exception?>(null)
    private val eWindow = ExceptionWindow(winManager)
    private val textColor = mutableStateOf(GREY_C)

    //?????????????? ?????????????? ????????
    @Composable
    override fun render() {
        Window(
            onCloseRequest = { winManager.closeAplUplWindow() },
            title = "???????????????? ?????????????????? ????????????",
            state = WindowState(width = WIDTH, height = HEIGHT)
        ) {
            //???????? ???????????????????? ?????????????? ?? ??????????????
            Box(
                Modifier.background(BACKGROUND_C).padding(15.dp)
            ) {

                //???????????????? ???????? ????????????????????
                if (openingException.value != null) {
                    eWindow.e = openingException.value
                    eWindow.render()
                    if (eWindow.finished.value) {
                        openingException.value = null
                        eWindow.finished.value = false
                    }
                }

                //?????????????????? ?????? ??????????????????, ???????? ?????? ????????
                if (openingApplication.value != -1) {
                    openTeamApplication()
                }

                //???????????????????? ???????????? ??????????
                val scrollState = rememberScrollState()

                //?????????????? ?? ?????????????????? ??????????????
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                    Arrangement.spacedBy(DEL_SHIFT),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    //???????????? ??????
                    Row(modifier = Modifier.wrapContentWidth(), Arrangement.spacedBy(DEL_SHIFT), Alignment.Top) {
                        //???????? ?? ??????????????????
                        CompetitionDataField(
                            Modifier.size(HEADER_FIELD_WIDTH, BTN_HEIGHT),
                            "???????????????? ????????????????????????",
                            competitionName.value
                        ) {
                            competitionName.value = it.filter { ch -> ch != ',' }
                        }
                        //???????? ?? ??????????
                        CompetitionDataField(
                            Modifier.size(HEADER_FIELD_WIDTH, BTN_HEIGHT),
                            "???????? ???????????????????????? (YYYY-MM-DD)",
                            competitionDate.value
                        ) {
                            competitionDate.value = it.filter { ch -> (('0'..'9') + '-').contains(ch) }
                        }
                        //?????????? ??????????????????
                        RaceSelector(modifier = Modifier.width(HEADER_FIELD_WIDTH))
                        val fileDialog = FileDialog(ComposeWindow())
                        //???????????? ???????????????? ?????????????? ??????????????????
                        CreateAplicationButton(Modifier.align(Alignment.CenterVertically)) {
                            if (metaInfoUploaded.value) {
                                teamApplications += TeamApplication("", emptyList(), 1)
                                teamApplicationsNames += ""
                                teamApplicationsSize.value = teamApplications.size
                            }
                        }
                        //???????????? ???????????????? ??????????????????
                        DownloadFilesButton(Modifier.align(Alignment.CenterVertically)) {
                            if (metaInfoUploaded.value) {
                                fileDialog.isMultipleMode = true
                                fileDialog.isVisible = true
                                teamApplications += getTeamApplicationsFromUser(fileDialog) ?: listOf()
                                teamApplicationsSize.value = teamApplications.size
                            }
                        }

                        //???????????? ???????????????????? ???????????????????? (???????????????????? ?? ?????? ??????????)
                        SaveButton(Modifier.align(Alignment.CenterVertically)) {
                            //???????????????????? ????????????????????
                            if (!metaInfoUploaded.value) {
                                val e = checkCompetitionData()
                                if (e == null) {
                                    metaInfoUploaded.value = true
                                    winManager.saveMetaInfo(
                                        MetaInfo(
                                            competitionName.value,
                                            LocalDate.parse(competitionDate.value),
                                            SportType.get(competitionSportType.value)
                                        )
                                    )
                                } else {
                                    eWindow.finished.value = false
                                    openingException.value = e
                                }
                            } //???????????????????? ?????? ????????????????????
                            else  {
                                if (teamApplications.isNotEmpty()) {
                                    winManager.saveApplication(teamApplications.toList())
                                    winManager.saveCompetition()
                                    winManager.createStartProtocols()
                                    winManager.addCompetitionName(competitionName.value)
                                    winManager.saveSerialization()
                                    winManager.openCompetitionWindow()
                                    winManager.closeAplUplWindow()
                                }
                            }
                        }
                    }
                    //???????????? ????????????
                    Column(
                        modifier = Modifier.fillMaxSize(0.90f),
                        Arrangement.spacedBy(DEL_SHIFT),
                        Alignment.CenterHorizontally
                    ) {
                        repeat(teamApplicationsSize.value) { i ->
                            Row(Modifier.wrapContentWidth(), Arrangement.spacedBy(DEL_SHIFT), Alignment.Top) {
                                val teamN = mutableStateOf(teamApplications[i].teamName)
                                ApplicationNameField(teamN, Modifier.height(BTN_HEIGHT).width(FILE_FIELD_WIDTH)) {
                                    teamN.value = it
                                    teamApplications[i].teamName = it
                                }
                                ApplicationButton(Modifier.height(BTN_HEIGHT).width(BTN_WIDTH)) {
                                    openingApplication.value = i
                                }
                                DeleteApplicationButton {
                                    teamApplications.removeAt(i)
                                    teamApplicationsNames.removeAt(i)
                                    teamApplicationsSize.value--
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
    //?????????????? ???????????????????? ???????????? ???? ???????? ?? ???????????????? ???? ???? ????????????????????????
    private fun getTeamApplicationsFromUser(fileDialog: FileDialog): List<TeamApplication>? {
        try {
            val result = fileDialog.files
                .mapIndexed { idx, it -> TeamApplication(it, idx + teamApplicationsSize.value) }
                .filter { !teamApplicationsNames.contains(it.teamName) }
            teamApplicationsNames += result.map { it.teamName }
            return result
        } catch (e: Exception) {
            openingException.value = e
        }
        return null
    }

    //???????????????? ??????????????????????????
    private fun checkCompetitionData(): Exception? {
        if (SportType.get(competitionSportType.value) == SportType.X) {
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

    //???????????????? ???????? ?? ??????????????????????
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

    @Composable //???????? ?? ????????????????????
    private fun ApplicationNameField(name: MutableState<String>, modifier: Modifier, onValueChange: (String) -> Unit) {
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

    @Composable //?????????????????? ??????
    private fun SaveButton(modifier: Modifier, onClick: () -> Unit) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = ACCENT_C)
        }
    }

    @Composable //???????????? ???????????????????? ????????????
    private fun DownloadFilesButton(modifier: Modifier, onClick: () -> Unit) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            Icon(
                Icons.Default.Search, contentDescription = null, tint =
                if (metaInfoUploaded.value) {
                    ACCENT_C
                } else {
                    GREY_C
                }
            )
        }
    }

    @Composable //???????????? ???????????????? ?????????????? ??????????????????
    private fun CreateAplicationButton(modifier: Modifier, onClick: () -> Unit) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = if (metaInfoUploaded.value) {
                    ACCENT_C
                } else {
                    GREY_C
                }
            )
        }
    }

    @Composable //???????????? ?????????????????? ????????????????
    private fun ApplicationButton(modifier: Modifier, onClick: () -> Unit) {
        IconButton(
            modifier = modifier,
            onClick = onClick,
        ) { Icon(Icons.Default.Create, contentDescription = null, tint = ACCENT_C) }
    }

    @Composable //???????????? ???????????????? ??????????????????
    private fun DeleteApplicationButton(onClick: () -> Unit) {
        IconButton(
            modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
            onClick = onClick,
        ) { Icon(Icons.Default.Delete, contentDescription = null, tint = ACCENT_C) }
    }


    @Composable //?????????????????????? ???????? ???????????? ???????? ????????????????????????
    private fun RaceSelector(modifier: Modifier) {
        Column {
            Button(
                modifier = modifier.height(BTN_HEIGHT),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = FOREGROUND_C
                ),
                onClick = {
                    if (!metaInfoUploaded.value) {
                        menuIsExpanded.value = true
                    }
                }
            ) {
                Text(
                    text = competitionSportType.value.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    style = TextStyle(textColor.value),
                    fontSize = 17.sp,
                )

            }
            //???????? ???????????????????? ??????????????
            DropdownMenu(
                modifier = modifier.background(FOREGROUND_C),
                expanded = menuIsExpanded.value,
                onDismissRequest = { menuIsExpanded.value = false }
            ) {
                Divider(color = BACKGROUND_C, thickness = 0.5.dp)
                SportType.values().dropLast(1).forEach {
                    DropdownMenuItem(onClick = {
                        competitionSportType.value = it.toRussian()
                        textColor.value = TEXT_C
                        menuIsExpanded.value = false
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

    //???????????? ???????????? 2 ????????????????????
    @Composable
    private fun CompetitionDataField(
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
            readOnly = metaInfoUploaded.value,
            placeholder = {
                Text(modifier = Modifier.width(WIDTH_OF_TEXT), text = textOfUnit, color = GREY_C)
            }
        )
    }
}