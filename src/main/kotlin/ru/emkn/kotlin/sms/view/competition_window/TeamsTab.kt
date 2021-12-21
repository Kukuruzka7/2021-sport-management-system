package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import ru.emkn.kotlin.sms.model.Team
import ru.emkn.kotlin.sms.model.athlete.toStringListWithShortDate
import ru.emkn.kotlin.sms.view.ClickableTexxxt
import ru.emkn.kotlin.sms.view.ColorScheme
import ru.emkn.kotlin.sms.view.table_view.TableContent
import ru.emkn.kotlin.sms.view.table_view.TableType

class TeamsTab(private val teamList: List<Team>, _modifier: Modifier) : TabLetka<Team>(_modifier, teamList) {
    private val dialog: MutableState<Team?> = mutableStateOf(null)

    @Composable
    override fun postrender() {
        when (dialog.value) {
            null -> {}
            else -> {
                val team = dialog.value!!
                Dialog(
                    onCloseRequest = { dialog.value = null },
                    title = team.name,
                    state = rememberDialogState(
                        width = DIALOG_WIDTH, height = DIALOG_HEIGHT
                    ),
                ) {
                    val horizontalState = rememberScrollState(0)
                    val verticalState = rememberScrollState(0)
                    Box(Modifier.fillMaxSize().background(color = ColorScheme.BACKGROUND_C)) {
                        TableContent(
                            TableType.TEAM,
                            Modifier.align(Alignment.Center).verticalScroll(verticalState)
                                .horizontalScroll(horizontalState),
                            team.toCSV()
                        )
                        HorizontalScrollbar(
                            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                            adapter = rememberScrollbarAdapter(scrollState = horizontalState),
                            style = ScrollbarStyle(
                                hoverColor = ColorScheme.SCROLLBAR_HOVER_C,
                                unhoverColor = ColorScheme.SCROLLBAR_UNHOVER_C,
                                minimalHeight = 16.dp,
                                thickness = 8.dp,
                                shape = RoundedCornerShape(4.dp),
                                hoverDurationMillis = 300,
                            )
                        )
                        VerticalScrollbar(
                            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                            adapter = rememberScrollbarAdapter(scrollState = verticalState),
                            style = ScrollbarStyle(
                                hoverColor = ColorScheme.SCROLLBAR_HOVER_C,
                                unhoverColor = ColorScheme.SCROLLBAR_UNHOVER_C,
                                minimalHeight = 16.dp,
                                thickness = 8.dp,
                                shape = RoundedCornerShape(4.dp),
                                hoverDurationMillis = 300,
                            )
                        )
                    }
                }
            }
        }
    }

    override fun split() =
        List(N_COLUMNS) { i -> teamList.filterIndexed { index, _ -> index % N_COLUMNS == i } }

    @Composable
    override fun toLink(element: Team) {
        ClickableTexxxt(modifier, element.name, style) { dialog.value = element }
    }


    private companion object {
        val N_COLUMNS = 2

        val DIALOG_WIDTH = 1000.dp
        val DIALOG_HEIGHT = 500.dp
        val DIALOG_PADDING = 20.dp

        fun Team.toCSV(): List<List<String>> = this.athletes.map { it.toStringListWithShortDate() }
    }
}