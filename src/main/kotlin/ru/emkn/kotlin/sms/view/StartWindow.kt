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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import ru.emkn.kotlin.sms.view.ColorScheme.ACCENT_C
import ru.emkn.kotlin.sms.view.ColorScheme.BACKGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C


interface StartWindowManager : WindowManager {
    fun closeStartWindow()

    fun openAplUplWindow()

    fun openCompetitionWindow(name: String)

    fun getCompetitionsNames(): List<String>

    fun giveCompetitionNameToModel(name: String)

}

class StartWindow(private val winManager: StartWindowManager) : IWindow(winManager) {
    private val isNameSelectorOpen = mutableStateOf(false)
    private val competitionNameSelectorWindow = CompetitionOpeningWindow(winManager)

    companion object {
        val WIDTH = 700.dp
        val HEIGHT = 400.dp
        val WELCOME_SIGN_PADDING = 50.dp
        val BUTTONS_OFFSET = 35.dp
    }

    @Composable
    override fun render() {
        Window(
            onCloseRequest = { winManager.closeStartWindow() },
            state = WindowState(
                width = WIDTH,
                height = HEIGHT
            ),
        ) {

            Box(Modifier.fillMaxSize().background(BACKGROUND_C)) {

                if (isNameSelectorOpen.value) {
                    competitionNameSelectorWindow.render()
                    if (competitionNameSelectorWindow.finished.value) {
                        isNameSelectorOpen.value = false
                        competitionNameSelectorWindow.finished.value = false
                    }
                }

                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    WelcomeSign(Modifier.padding(vertical = WELCOME_SIGN_PADDING))
                    Row(
                        modifier = Modifier.fillMaxSize().offset(y = BUTTONS_OFFSET),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Top
                    ) {
                        CreateButton()
                        OpenButton()
                    }
                }
            }
        }
    }


    @Composable
    private fun StartWindowButton(text: String, icon: ImageVector, onClick: () -> Unit) {
        val CORNERS = 4.dp
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
                    tint = ACCENT_C
                )
            }
            Text(text, color = TEXT_C)
        }
    }

    @Composable
    private fun CreateButton() = StartWindowButton("Создать", Icons.Filled.Add) {
        winManager.openAplUplWindow()
        winManager.closeStartWindow()
    }

    @Composable
    private fun OpenButton() = StartWindowButton("Открыть", Icons.Default.PlayArrow) {
        isNameSelectorOpen.value = true
    }


    @Composable
    private fun WelcomeSign(modifier: Modifier) {
        val TEXT = "Добро пожаловать в sport management system!"
        val FONT_SIZE = 22.sp
        Text(
            text = TEXT,
            modifier = modifier,
            color = TEXT_C,
            fontSize = FONT_SIZE,
            fontWeight = FontWeight.Bold
        )
    }
}