package ru.emkn.kotlin.sms.view

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.input_result.InputCompetitionResult
import ru.emkn.kotlin.sms.model.input_result.InputCompetitionResultByAthletes
import ru.emkn.kotlin.sms.model.input_result.InputCompetitionResultByCheckPoints
import ru.emkn.kotlin.sms.view.ColorScheme.BACKGROUND_C
import ru.emkn.kotlin.sms.view.table_view.TableContent
import ru.emkn.kotlin.sms.view.table_view.TableType
import ru.emkn.kotlin.sms.view.table_view.toMListMListStr
import java.awt.FileDialog
import java.io.File

interface ResUplWinManager : WindowManager {
    fun closeResUplWindow()
    fun saveResults(files: List<File>)
}

enum class ResType {
    BY_CHECKPOINTS,
    BY_ATHLETES,
}

class ResultUploadingWindow(model: Model, private val winManager: ResUplWinManager, private val resType: ResType) : IWindow(winManager) {
    private val openingResult = mutableStateOf(false)
    private val result: MutableState<InputCompetitionResult?> = mutableStateOf(null)
    private val finished = mutableStateOf(false)
    private val competition: Competition = TODO()

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
                if (openingResult.value) {
                    openResult()
                }

                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                    Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.Top) {
                        val fileDialog = FileDialog(ComposeWindow())
                        NewFilesButton(Modifier.align(Alignment.CenterVertically)) {
                            fileDialog.isVisible = true
                            result.value = when(resType){
                                ResType.BY_ATHLETES -> {
                                    InputCompetitionResultByAthletes(fileDialog.files.first().path)
                                }
                                ResType.BY_CHECKPOINTS -> {
                                    InputCompetitionResultByCheckPoints(fileDialog.files.first().path, competition)
                                }
                            }
                        }
                        SaveButton(Modifier.align(Alignment.CenterVertically)) {
                            finished.value = true
                        }
                    }
                    Row(Modifier.wrapContentWidth(), Arrangement.spacedBy(DEL_SHIFT), Alignment.Top) {
                        FileButton("Результаты") { }
                        DeleteFileButton {
                            result.value = null
                        }
                    }

                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState = scrollState),
                    style = ScrollbarStyle(
                        hoverColor = ColorScheme.SCROLLBAR_HOVER_C, unhoverColor = ColorScheme.SCROLLBAR_UNHOVER_C,
                        minimalHeight = 16.dp, thickness = 8.dp,
                        shape = RoundedCornerShape(4.dp), hoverDurationMillis = 300,
                    )
                )
            }
        }
    }

    @Composable
    private fun openResult() {
        val table = result.value
        require(table != null)
        val rows = table.rows
        val name = table.fileName
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
                result.value = TODO()
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
        val UPPL_GAP = 5.dp
        val DEL_SHIFT = 5.dp
        val MAIN_BUTTONS_GAP = 5.dp
        val WIDTH = 1500.dp
        val HEIGHT = 1000.dp
        val BTN_HEIGHT = 50.dp
        val BTN_WIDTH = 50.dp
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
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = ColorScheme.ACCENT_C)
        }
    }

    @Composable
    private fun DeleteFileButton(onClick: () -> Unit) {
        IconButton(
            modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
            onClick = onClick,
        ) { Icon(Icons.Default.Delete, contentDescription = null, tint = ColorScheme.ACCENT_C) }
    }

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

}