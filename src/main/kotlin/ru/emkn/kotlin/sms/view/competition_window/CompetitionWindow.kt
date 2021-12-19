package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.view.*
import ru.emkn.kotlin.sms.view.ColorScheme.ACCENT_C
import ru.emkn.kotlin.sms.view.ColorScheme.BACKGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.FOREGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.GREY_C
import ru.emkn.kotlin.sms.view.ColorScheme.SCROLLBAR_HOVER_C
import ru.emkn.kotlin.sms.view.ColorScheme.SCROLLBAR_UNHOVER_C
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C

interface CompetitionWindowsManager : WindowManager {
    fun closeCompWindow()
    fun openCSV(fileName: String)
}


class CompetitionWindow(private val model: Model, private val winManager: CompetitionWindowsManager) :
    IWindow(winManager) {

    companion object {
        val WIDTH = 700.dp
        val HEIGHT = 700.dp
        val SPACING = 30.dp
    }

    @Composable
    override fun render() {
        require(model.competition != null)
        val info = model.competition!!.info
        Window(
            onCloseRequest = { winManager.closeCompWindow() }, title = info.name, state = WindowState(
                width = WIDTH, height = HEIGHT
            )
        ) {
            Box(Modifier.fillMaxSize().background(BACKGROUND_C).padding(SPACING)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(SPACING)
                ) {
                    CompetitionName(info.name)
                    InfoBlock(info, model.stage.value).render()
                    MainBlock(model).render()
                }
            }
        }
    }

    @Composable
    private fun CompetitionName(name: String) {
        val FONT_SIZE = 22.sp

        Text(
            text = name, color = TEXT_C, fontSize = FONT_SIZE, fontWeight = FontWeight.Bold
        )
    }


    class InfoBlock(private val info: MetaInfo, private val stage: Model.Companion.Stage) {
        @Composable
        fun render() {
            Box(modifier = Modifier.clip(RoundedCornerShape(CORNERS)).background(FOREGROUND_C)) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = VERTICAL_PADDING, horizontal = HORIZONTAL_PADDING),
                    verticalArrangement = Arrangement.spacedBy(SPACING)
                ) {
                    TextBlock("вид спорта", info.sport.toRussian())
                    Divider(color = GREY_C, thickness = DIVIDER_THICKNESS)
                    TextBlock("дата проведения", info.date.toString())
                    Divider(color = GREY_C, thickness = DIVIDER_THICKNESS)
                    TextBlock("текущее состояние", stage.toRussian())
                }
            }
        }

        private companion object {
            val CORNERS = 15.dp
            val VERTICAL_PADDING = 10.dp
            val HORIZONTAL_PADDING = 8.dp
            val SPACING = 7.dp
            val DIVIDER_THICKNESS = 0.5.dp
        }

        @Composable
        fun TextBlock(smallText: String, bigText: String) {
            val SMALL_TEXT_SIZE = 10.sp
            val BIG_TEXT_SIZE = 18.sp
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text(
                    text = smallText,
                    color = ACCENT_C,
                    fontSize = SMALL_TEXT_SIZE,
                )
                Text(
                    text = bigText,
                    color = TEXT_C,
                    fontSize = BIG_TEXT_SIZE,
                )
            }
        }

        fun Model.Companion.Stage.toRussian(): String = when (this) {
            Model.Companion.Stage.ONGOING -> "идёт"
            Model.Companion.Stage.FINISHED -> "завершено"
        }
    }

    class MainBlock(val model: Model) {
        val tabState = mutableStateOf(TabEnum.GROUPS)
        private val tabFactory = TabFactory(model)

        @Composable
        fun render() {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(CORNERS)).background(color = FOREGROUND_C)
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier.fillMaxSize().absolutePadding(bottom = PADDING)
                        .verticalScroll(state = scrollState)
                ) {
                    TopPanel(tabState)
                    Divider(color = GREY_C, thickness = DIVIDER_THICKNESS)
                    tabFactory.get(tabState, Modifier.fillMaxSize()).render()
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

        companion object {
            val CORNERS = 15.dp
            val PADDING = 15.dp
            val DIVIDER_THICKNESS = 0.5.dp
        }

        @Composable
        private fun TopPanel(tabState: MutableState<TabEnum>) {
            val VERTICAL_PADDING = 13.dp
            val FONT_SIZE = 15.sp
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabEnum.values().forEach { tabEnum ->
                    val style = SpanStyle(
                        fontSize = FONT_SIZE,
                        color = if (tabEnum == tabState.value) {
                            ACCENT_C
                        } else {
                            TEXT_C
                        }
                    )
                    ClickableText(
                        modifier = Modifier.padding(vertical = VERTICAL_PADDING),
                        text = tabToRussian(tabEnum),
                        style = style,
                        onClick = { tabState.value = tabEnum })

                }
            }
        }

        private fun tabToRussian(tabEnum: TabEnum): String = when (tabEnum) {
            TabEnum.GROUPS -> "Группы"
            TabEnum.TEAMS -> "Команды"
            TabEnum.ATHLETES -> "Спортсмены"
            TabEnum.START_PROTOCOLS -> "Стартовые протоколы"
            TabEnum.RESULT -> "Результаты"
        }
    }
}