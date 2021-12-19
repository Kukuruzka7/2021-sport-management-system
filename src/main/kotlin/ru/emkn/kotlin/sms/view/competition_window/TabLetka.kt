package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.emkn.kotlin.sms.view.ClickableText
import ru.emkn.kotlin.sms.view.ColorScheme

data class Hyperlink(val name: String, val onClick: () -> Unit)

abstract class TabLetka(private val links: List<Hyperlink>, _modifier: Modifier) : ITab(_modifier) {
    val SPACING = 10.dp
    val FONT_SIZE = 17.sp
    val OFFSET = 9.dp


    @Composable
    override fun render() {
        Row(modifier = modifier.fillMaxSize().offset(y = OFFSET), horizontalArrangement = Arrangement.SpaceEvenly) {
            split(links).forEach {
                LinkColumn(it)
            }
        }
    }

    @Composable
    fun LinkColumn(list: List<Hyperlink>) {
        val style: SpanStyle = SpanStyle(color = ColorScheme.TEXT_C, fontSize = FONT_SIZE)
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(SPACING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            list.forEach() {
                ClickableText(Modifier, it.name, style, it.onClick)
            }
        }
    }

    abstract fun split(linksList: List<Hyperlink>): List<List<Hyperlink>>
}

class GroupsTab(links: List<Hyperlink>, _modifier: Modifier) : TabLetka(links, _modifier) {

    override fun split(linksList: List<Hyperlink>): List<List<Hyperlink>> =
        linksList.groupBy { it.name[0] }.values.toList().reversed()

}

class TeamsTab(links: List<Hyperlink>, _modifier: Modifier) : TabLetka(links, _modifier) {
    private val N_COLUMNS = 2

    override fun split(linksList: List<Hyperlink>): List<List<Hyperlink>> =
        List(N_COLUMNS) { i -> linksList.filterIndexed { index, _ -> index % N_COLUMNS == i } }

}

class AthletesTab(links: List<Hyperlink>, _modifier: Modifier) : TabLetka(links, _modifier) {
    private val N_COLUMNS = 3

    override fun split(linksList: List<Hyperlink>): List<List<Hyperlink>> =
        List(N_COLUMNS) { i -> linksList.filterIndexed { index, _ -> index % N_COLUMNS == i } }
}

