package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.mouse.MouseScrollUnit
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.MetaInfo

interface CompetitionWindowsManager : WindowManager {
    fun closeCompWindow()
}

class CompetitionWindow(val competition: Competition, val winManager: CompetitionWindowsManager) :
    IWindow(winManager) {
    companion object {
        val WIDTH = 700.dp
        val HEIGHT = 400.dp
        val BACKGROUND_COLOR = Color(0xF1111111)
        val TEXT_COLOR = Color(0xF1dddddd)
        val SPACING = 30.dp
    }

    @Composable
    override fun render() {
        val info = competition.info
        Window(
            onCloseRequest = { winManager.closeCompWindow() },
            title = "lel",
            state = WindowState(
                width = WIDTH,
                height = HEIGHT
            )
        ) {
            Box(Modifier.fillMaxSize().background(BACKGROUND_COLOR).padding(SPACING)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(SPACING)
                ) {
                    CompetitionName(info.name, Modifier).render()
                    InfoBlock(info, Modifier).render()
                }
            }
        }
    }

    class CompetitionName(val name: String, val modifier: Modifier) {
        @Composable
        fun render() {
            Text(
                text = name,
                modifier = modifier,
                color = TEXT_COLOR,
                fontSize = FONT_SIZE,
                fontWeight = FontWeight.Bold
            )
        }

        companion object {
            private val FONT_SIZE = 22.sp
        }
    }

    class InfoBlock(val info: MetaInfo, val modifier: Modifier) {
        @Composable

        fun render() {
            Box(modifier = Modifier.clip(RoundedCornerShape(CORNERS)).background(BACKGROUND_COLOR)) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = VERTICAL_PADDING, horizontal = HORIZONTAL_PADDING),
                    verticalArrangement = Arrangement.spacedBy(SPACING)
                ) {
                    TextBlock(Modifier, "вид спорта", info.sport.toString()).render()
                    TextBlock(Modifier, "дата проведения", info.date.toString()).render()
                    TextBlock(Modifier, "текущее состояние", "идёт").render()
                }
            }
        }

        companion object {
            val CORNERS = 15.dp
            val BACKGROUND_COLOR = Color(0xF1282828)
            val VERTICAL_PADDING = 10.dp
            val HORIZONTAL_PADDING = 8.dp
            val SPACING = 10.dp
        }

        class TextBlock(val modifier: Modifier, val smallText: String, val bigText: String) {
            @Composable
            fun render() {
                Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                    Text(
                        text = smallText,
                        modifier = Modifier,
                        color = SMALL_TEXT_COLOR,
                        fontSize = SMALL_TEXT_SIZE,
                    )
                    Text(
                        text = bigText,
                        modifier = Modifier,
                        color = BIG_TEXT_COLOR,
                        fontSize = BIG_TEXT_SIZE,
                    )

                }
            }

            companion object {
                val SMALL_TEXT_SIZE = 10.sp
                val SMALL_TEXT_COLOR = Color(0xF12A7BF6)
                val BIG_TEXT_SIZE = 18.sp
                val BIG_TEXT_COLOR = Color(0xF1dddddd)
            }
        }

    }

}