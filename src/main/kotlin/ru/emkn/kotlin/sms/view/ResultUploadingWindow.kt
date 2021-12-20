package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import ru.emkn.kotlin.sms.FileDoNotDownload
import ru.emkn.kotlin.sms.InvalidSportType
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.SportType
import ru.emkn.kotlin.sms.model.input_result.InputCompetitionResult
import ru.emkn.kotlin.sms.model.input_result.InputCompetitionResultByAthletes
import ru.emkn.kotlin.sms.model.input_result.InputCompetitionResultByCheckPoints
import ru.emkn.kotlin.sms.view.ColorScheme.BACKGROUND_C
import ru.emkn.kotlin.sms.view.table_view.TableContent
import ru.emkn.kotlin.sms.view.table_view.TableType
import ru.emkn.kotlin.sms.view.table_view.toListListStr
import ru.emkn.kotlin.sms.view.table_view.toMListMListStr
import java.awt.FileDialog
import java.io.File

interface ResUplWinManager : WindowManager {
    fun closeResUplWindow()
    fun saveResults(files: List<File>)
    fun getCompetitionsNames(): List<String>
}

enum class ResType {
    BY_CHECKPOINTS,
    BY_ATHLETES,
}

class ResultUploadingWindow(model: Model, private val winManager: ResUplWinManager, private val resType: ResType) :
    IWindow(winManager) {
    private val openingResult = mutableStateOf(false)
    private val result: MutableState<InputCompetitionResult?> = mutableStateOf(null)
    private val openingException = mutableStateOf<Exception?>(null)
    val finished = mutableStateOf(false)
    val eWindow = ExceptionWindow(winManager)
    private val competition: Competition =
        model.competition ?: throw Exception("ResultUploadingWindow получил model без competition")

    @Composable
    override fun render() {
        Window(
            onCloseRequest = { winManager.closeResUplWindow() },
            title = "Загрузка результатов",
            state = WindowState(width = WIDTH, height = HEIGHT)
        ) {
            Box(
                Modifier.background(BACKGROUND_C)
            ) {

                if (openingException.value != null) {
                    eWindow.e = openingException.value
                    eWindow.render()
                    if (eWindow.finished.value) {
                        openingException.value = null
                    }
                }

                if (openingResult.value) {
                    openResult()
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.Top) {
                        val fileDialog = FileDialog(ComposeWindow())
                        NewFilesButton(Modifier.align(Alignment.CenterVertically)) {
                            fileDialog.isVisible = true
                            try {
                                result.value = when (resType) {
                                    ResType.BY_ATHLETES -> {
                                        InputCompetitionResultByAthletes(fileDialog.files.first().path)
                                    }
                                    ResType.BY_CHECKPOINTS -> {
                                        InputCompetitionResultByCheckPoints(fileDialog.files.first().path, competition)
                                    }
                                }
                                openingResult.value = true
                            } catch (e: Exception) {
                                eWindow.finished.value = false
                                openingException.value = e
                            }
                        }
                        SaveButton(Modifier.align(Alignment.CenterVertically)) {
                            val e = checkFileDownload()
                            if (e == null) {
                                finished.value = true
                            } else {
                                eWindow.finished.value = false
                                openingException.value = e
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkFileDownload(): Exception? {
        if (result.value == null) {
            return FileDoNotDownload()
        }
        return null
    }

    @Composable
    private fun openResult() {
        val table = result.value
        require(table != null)
        val rows = table.rows
        val tableCells = rows.toMListMListStr()

        Box(Modifier.fillMaxSize().background(BACKGROUND_C), Alignment.CenterEnd) {
            val tableStateVertical = rememberScrollState(0)
            val tableStateHorizontal = rememberScrollState(0)
            TableContent(
                type = TableType.CHECKPOINT_RES,
                modifier = Modifier.wrapContentSize()
                    .align(Alignment.TopCenter)
                    .verticalScroll(tableStateVertical)
                    .horizontalScroll(tableStateHorizontal),
                contentRows = tableCells
            ) {
                try {
                    result.value = when (resType) {
                        ResType.BY_ATHLETES -> {
                            InputCompetitionResultByAthletes(tableCells.toListListStr())
                        }
                        ResType.BY_CHECKPOINTS -> {
                            InputCompetitionResultByCheckPoints(tableCells.toListListStr(), competition)
                        }
                    }
                } catch (e: Exception) {
                    eWindow.finished.value = false
                    openingException.value = e
                }
            }
            HorizontalScrollbar(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                adapter = rememberScrollbarAdapter(scrollState = tableStateHorizontal),
                style = ScrollbarStyle(
                    hoverColor = ColorScheme.SCROLLBAR_HOVER_C, unhoverColor = ColorScheme.SCROLLBAR_UNHOVER_C,
                    minimalHeight = 16.dp, thickness = 8.dp,
                    shape = RoundedCornerShape(4.dp), hoverDurationMillis = 300,
                )
            )
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState = tableStateVertical),
                style = ScrollbarStyle(
                    hoverColor = ColorScheme.SCROLLBAR_HOVER_C, unhoverColor = ColorScheme.SCROLLBAR_UNHOVER_C,
                    minimalHeight = 16.dp, thickness = 8.dp,
                    shape = RoundedCornerShape(4.dp), hoverDurationMillis = 300,
                )
            )
        }

    }

    private companion object {
        val WIDTH = 1500.dp
        val HEIGHT = 1000.dp
        val BTN_HEIGHT = 50.dp
        val FILE_BTN_WIDTH = 500.dp
        val CORNERS = 4.dp
    }

    @Composable
    private fun SaveButton(modifier: Modifier, onClick: () -> Unit) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = ColorScheme.ACCENT_C)
        }
    }

    @Composable
    private fun NewFilesButton(modifier: Modifier, onClick: () -> Unit) {
        val text = "Выбрать файл с результатами"
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = ColorScheme.FOREGROUND_C),
            modifier = modifier
                .clip(RoundedCornerShape(CORNERS))
                .size(FILE_BTN_WIDTH, BTN_HEIGHT),
            onClick = onClick
        ) {
            Text(text = text, color = ColorScheme.TEXT_C)
        }
    }
}