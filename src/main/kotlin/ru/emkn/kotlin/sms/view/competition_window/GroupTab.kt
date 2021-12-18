package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.emkn.kotlin.sms.Group
import ru.emkn.kotlin.sms.view.ClickableText
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C

data class GroupHyperlink(val name: String, val csvName: String)

class GroupTab(private val groupLinks: List<GroupHyperlink>, _modifier: Modifier) : ITab(TabEnum.GROUPS, _modifier) {
    @Composable
    override fun render() {
        Row(modifier = modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
            LinkColumn(groupLinks.filterIndexed { index, _ -> index % 2 == 0 })
            LinkColumn(groupLinks.filterIndexed { index, _ -> index % 2 == 1 })
        }
    }

    @Composable
    fun LinkColumn(list: List<GroupHyperlink>) {
        val style: SpanStyle = SpanStyle(color = TEXT_C, fontSize = FONT_SIZE)
        Column(
            modifier = modifier.fillMaxSize().offset(y = 10.dp),
            verticalArrangement = Arrangement.spacedBy(SPACING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            list.forEach() {
                ClickableText(Modifier, it.name, style) {}
            }
        }
    }

    private companion object {
        val SPACING = 10.dp
        val FONT_SIZE = 17.sp
    }
}