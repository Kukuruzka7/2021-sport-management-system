package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import ru.emkn.kotlin.sms.view.application_view.ApplicationUploadingWindow
import javax.swing.event.TreeWillExpandListener

class StartWindow : IWindow {
    override fun render() {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = ":)",
                state = WindowState(
                    width = WIDTH,
                    height = HEIGHT
                )
            ) {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    WelcomeSign(Modifier).render()
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Top
                    ) {
                        CreateButton { }.render()
                        OpenButton {}.render()
                    }
                }
            }
        }
    }

    companion object {
        val WIDTH = 500.dp
        val HEIGHT = 400.dp
    }

    open class StartWindowButton(
        private val onClick: () -> Unit,
        private val text: String,
        private val icon: ImageVector
    ) : IButton {
        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        override fun render() {
            /*Button(
                modifier = Modifier
                    .clip(RoundedCornerShape(CORNERS))
                    .focusable(interactionSource = remember { MutableInteractionSource() }),
                onClick = onClick

             */
            Column {
                IconButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(CORNERS))
                        .focusable(interactionSource = remember { MutableInteractionSource() }),
                    onClick = onClick,
                ) {
                    Icon(
                        icon,
                        "contentDescription",
                        tint = Color.DarkGray
                    )
                }
                Text(text)
            }
        }

        companion object {
            val CORNERS = 4.dp
        }
    }

    private class CreateButton(onClick: () -> Unit) : StartWindowButton(onClick, "Создать", Icons.Filled.Add)
    private class OpenButton(onClick: () -> Unit) : StartWindowButton(onClick, "Открыть", Icons.Default.ArrowDropDown)

    private class WelcomeSign(val modifier: Modifier) {
        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        fun render() {
            Text(text = TEXT, modifier = modifier, fontSize = FONT_SIZE, fontWeight = FontWeight.Bold)
        }

        companion object {
            private val TEXT = "Добро пожаловать в Sport Management System"
            private val FONT_SIZE = 20.sp
        }
    }
}