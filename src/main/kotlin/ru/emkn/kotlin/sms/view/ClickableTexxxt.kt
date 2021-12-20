package ru.emkn.kotlin.sms.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle


@Composable
fun ClickableTexxxt(modifier: Modifier, text: String, style: SpanStyle, onClick: () -> Unit) {
    val annotatedText = buildAnnotatedString {
        //append your initial text
        withStyle(
            style = style
        ) {
            append(text)
        }
    }
    androidx.compose.foundation.text.ClickableText(modifier = modifier, text = annotatedText, onClick = { onClick() })
}
