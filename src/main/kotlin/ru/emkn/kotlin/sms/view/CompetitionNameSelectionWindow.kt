package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.emkn.kotlin.sms.view.ColorScheme.BACKGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.FOREGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.SCROLLBAR_HOVER_C
import ru.emkn.kotlin.sms.view.ColorScheme.SCROLLBAR_UNHOVER_C
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C

interface CompNameSelectionWindowManager : WindowManager {
    fun getCompetitionsNames(): List<String>
    fun openCompetitionWindowByName(name: String)
    fun closeStartWindow()
}


class CompetitionNameSelectionWindow(private val winManager: CompNameSelectionWindowManager) : IWindow(winManager) {
    var chosenName: String? = null
    private val competitionNames = winManager.getCompetitionsNames()
    val finished = mutableStateOf(false)

    @Composable
    override fun render() {
        Dialog(
            onCloseRequest = { finished.value = true },
            title = "Выберете соревнование"
        ) {
            Box(modifier = Modifier.background(BACKGROUND_C).fillMaxSize(), Alignment.Center) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                    Arrangement.spacedBy((-10).dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    competitionNames.forEach { str ->
                        CompetitionButton(modifier = Modifier.align(Alignment.CenterHorizontally), str) {
                            finished.value = true
                            chosenName = str //выбранное для открытия соревнование с именем str
                            require(chosenName != null)
                            winManager.openCompetitionWindowByName(chosenName!!) //открытие окна с этим соревнованием
                            winManager.closeStartWindow()
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

    //кнопка с названием соревнований, которые уже существуют
    @Composable
    private fun CompetitionButton(modifier: Modifier, text: String, onClick: () -> Unit) {
        Button(
            modifier = modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = FOREGROUND_C),
            onClick = onClick
        ) {
            Text(text, color = TEXT_C)
        }
    }

}