package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import ru.emkn.kotlin.sms.view.button.IButton

interface StartWindowManager : WindowManager {
    fun closeStartWindow()

    fun openAplUplWindow()
}

class StartWindow(val winManager: StartWindowManager) : IWindow(winManager) {

    companion object {
        val WIDTH = 700.dp
        val HEIGHT = 400.dp
        val WELCOME_SIGN_PADDING = 50.dp
        val BUTTONS_OFFSET = 35.dp
        val BACKGROUND_COLOR = Color(0xF1111111)
        val TEXT_COLOR = Color(0xF1dddddd)
    }

    @Composable
    override fun render() {
        Window(
            onCloseRequest = { winManager.closeStartWindow() },
            title = "kek",
            state = WindowState(
                width = WIDTH,
                height = HEIGHT
            )
        ) {
            Box(Modifier.fillMaxSize().background(BACKGROUND_COLOR)) {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    WelcomeSign(Modifier.padding(vertical = WELCOME_SIGN_PADDING)).render()
                    Row(
                        modifier = Modifier.fillMaxSize().offset(y = BUTTONS_OFFSET),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Top
                    ) {
                        CreateButton {
                            winManager.openAplUplWindow()
                            winManager.closeStartWindow()
                        }.render()
                        OpenButton {}.render()
                    }
                }
            }
        }
    }

    open class StartWindowButton(
        private val text: String, private val icon: ImageVector, private val onClick: () -> Unit
    ) : IButton {
        @Composable
        override fun render() {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(CORNERS))
                        .focusable(interactionSource = remember { MutableInteractionSource() }),
                    onClick = onClick,
                ) {
                    Icon(
                        icon,
                        modifier = Modifier.size(40.dp),
                        contentDescription = "кнопочка",
                        tint = ICON_COLOR
                    )
                }
                Text(text, color = ICON_COLOR)
            }
        }

        companion object {
            val CORNERS = 4.dp
            val ICON_COLOR = Color(0xF12A7BF6)
        }
    }

    private class CreateButton(onClick: () -> Unit) : StartWindowButton("Создать", Icons.Filled.Add, onClick)
    private class OpenButton(onClick: () -> Unit) : StartWindowButton("Открыть", Icons.Default.PlayArrow, onClick)

    private class WelcomeSign(val modifier: Modifier) {
        @Composable
        fun render() {
            Text(
                text = TEXT,
                modifier = modifier,
                color = TEXT_COLOR,
                fontSize = FONT_SIZE,
                fontWeight = FontWeight.Bold
            )
        }

        companion object {
            private val TEXT = "Добро пожаловать в sport management system!"
            private val FONT_SIZE = 22.sp
        }
    }
}