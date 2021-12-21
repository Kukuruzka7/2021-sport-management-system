package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.emkn.kotlin.sms.view.ColorScheme
import ru.emkn.kotlin.sms.view.table_view.TableContent
import ru.emkn.kotlin.sms.view.table_view.TableType

class GroupsTab(val races: List<List<String>>, _modifier: Modifier) : ITab(_modifier) {
    @Composable
    override fun render() {
        Box(modifier.fillMaxSize(), Alignment.Center) {
            val horizontalState = rememberScrollState()
//            val verticalState = rememberScrollState()
            Box(Modifier.fillMaxSize(0.90f).align(Alignment.Center)) {
                TableContent(
                    TableType.COURSES,
                    Modifier.align(Alignment.Center).horizontalScroll(horizontalState)/*.verticalScroll(verticalState)*/,
                    races
                )
                HorizontalScrollbar(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                    adapter = rememberScrollbarAdapter(scrollState = horizontalState),
                    style = ScrollbarStyle(
                        hoverColor = ColorScheme.SCROLLBAR_HOVER_C, unhoverColor = ColorScheme.SCROLLBAR_UNHOVER_C,
                        minimalHeight = 16.dp, thickness = 8.dp,
                        shape = RoundedCornerShape(4.dp), hoverDurationMillis = 300,
                    )
                )
//                VerticalScrollbar(
//                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
//                    adapter = rememberScrollbarAdapter(scrollState = verticalState),
//                    style = ScrollbarStyle(
//                        hoverColor = ColorScheme.SCROLLBAR_HOVER_C, unhoverColor = ColorScheme.SCROLLBAR_UNHOVER_C,
//                        minimalHeight = 16.dp, thickness = 8.dp,
//                        shape = RoundedCornerShape(4.dp), hoverDurationMillis = 300,
//                    )
//                )
            }
        }
    }
}