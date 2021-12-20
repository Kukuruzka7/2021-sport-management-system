package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.emkn.kotlin.sms.view.ColorScheme

abstract class TabLetka<T>(_modifier: Modifier, private val elements: List<T>) : ITab(_modifier) {

    companion object {
        val SPACING = 10.dp
        val FONT_SIZE = 17.sp
        val OFFSET = 9.dp
        val style: SpanStyle = SpanStyle(color = ColorScheme.TEXT_C, fontSize = FONT_SIZE)
    }


    @Composable
    abstract fun toLink(element: T)


    @Composable
    override fun render() {
        prerender()
        postrender()
    }

    @Composable
    private fun prerender() {
        Row(modifier = modifier.fillMaxSize().offset(y = OFFSET), horizontalArrangement = Arrangement.SpaceEvenly) {
            split().forEach { LinkColumn(it) }
        }
    }

    @Composable
    abstract fun postrender()


    @Composable
    fun LinkColumn(list: List<T>) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(SPACING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            list.forEach() { toLink(it) }
            //ClickableText(Modifier, it.name, style, it.onClick)
        }
    }

    abstract fun split(): List<List<T>>
}
