package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import ru.emkn.kotlin.sms.Group
import ru.emkn.kotlin.sms.readCSV
import ru.emkn.kotlin.sms.view.ClickableTexxxt
import ru.emkn.kotlin.sms.view.ColorScheme
import ru.emkn.kotlin.sms.view.ColorScheme.BACKGROUND_C
import ru.emkn.kotlin.sms.view.table_view.TableContent
import ru.emkn.kotlin.sms.view.table_view.TableType

class StartProtocolsTab(
    private val groupList: List<Group>,
    _modifier: Modifier,
    val fileNameBuilder: (String) -> String
) :
    TabLetka<Group>(_modifier, groupList) {
    private val dialog: MutableState<String?> = mutableStateOf(null)

    @Composable
    override fun postrender() {
        when (dialog.value) {
            null -> {}
            else -> Dialog(
                onCloseRequest = { dialog.value = null },
                title = dialog.value.toString(),
                state = rememberDialogState(
                    width = DIALOG_WIDTH, height = DIALOG_HEIGHT
                ),
            ) {
                val scrollState = rememberScrollState()
                Box(Modifier.fillMaxSize().background(color = BACKGROUND_C).padding(DIALOG_PADDING)) {
                    TableContent(
                        TableType.START_PROTOCOL,
                        Modifier.align(Alignment.TopCenter).verticalScroll(state = scrollState),
                        readCSV(fileNameBuilder(dialog.value!!)).drop(1)
                    )
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
    }

    private companion object {
        val DIALOG_WIDTH = 750.dp
        val DIALOG_HEIGHT = 500.dp
        val DIALOG_PADDING = 20.dp
    }

    override fun split(): List<List<Group>> =
        groupList.groupBy { it.race.groupName[0] }.values.toList().reversed()

    @Composable
    override fun toLink(element: Group) {
        ClickableTexxxt(Modifier, element.race.groupName, style) { dialog.value = element.race.groupName }
    }

}