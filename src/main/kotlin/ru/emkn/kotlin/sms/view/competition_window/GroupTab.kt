package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.emkn.kotlin.sms.Group
import ru.emkn.kotlin.sms.view.ClickableText
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C

class GroupTab(private val groupList: List<Group>, _modifier: Modifier) : ITab(TabEnum.GROUPS, _modifier) {
    @Composable
    override fun render() {
        val style: SpanStyle = SpanStyle(color = TEXT_C, fontSize = FONT_SIZE)
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(SPACING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            groupList.forEach {
                ClickableText(Modifier, it.race.groupName.value, style) {}
            }
        }
    }

    private companion object {
        val SPACING = 10.dp
        val FONT_SIZE = 17.sp
    }
}