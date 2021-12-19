package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.w3c.dom.Text
import ru.emkn.kotlin.sms.view.ColorScheme.BACKGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.FOREGROUND_C
import ru.emkn.kotlin.sms.view.ColorScheme.GREY_C
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C
import ru.emkn.kotlin.sms.view.Model

class ResultsTab(_modifier: Modifier, private val model: Model) : ITab(_modifier) {
    @Composable
    override fun render() {
        require(model.competition != null)
        when (model.stage.value) {
            Model.Companion.Stage.FINISHED -> {
                val links = model.competition!!.groupList.map { Hyperlink(it.race.groupName.value) {} }
                WithResultTab(links, modifier).render()
            }
            Model.Companion.Stage.ONGOING -> NoResultTab(modifier).render()

        }
    }

    private class NoResultTab(private val modifier: Modifier) {
        @Composable
        fun render() {
            Column(
                modifier = modifier.padding(NO_RESULT_PADDING),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(SPACING)
            ) {
                Text(
                    text = NO_RESULT_TEXT,
                    fontSize = NO_RESULT_FONT_SIZE,
                    color = TEXT_C
                )
                Text(
                    text = SUGGESTION_TEXT,
                    fontSize = SUGGESTION_FONT_SIZE,
                    color = TEXT_C
                )
                Row() {
                    DownloadResultByAthletesButton(Modifier)
                    DownloadResultByCheckpointButton(Modifier)
                }
            }
        }

        private companion object {
            const val NO_RESULT_TEXT = "Результаты пока не были загружены в систему"
            val NO_RESULT_FONT_SIZE = 15.sp
            val NO_RESULT_PADDING = 10.dp
            const val SUGGESTION_TEXT = "Вы можете сделать это сейчас:"
            val SUGGESTION_FONT_SIZE = 12.sp
            val SPACING = 10.dp
        }

        @Composable
        private fun DownloadResultsButton(modifier: Modifier, text: String, onClick: () -> Unit) {
            Button(
                modifier = modifier,
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(backgroundColor = GREY_C)
            ) {
                Text(text = text, color = TEXT_C)
            }

        }

        @Composable
        private fun DownloadResultByAthletesButton(modifier: Modifier) =
            DownloadResultsButton(modifier, "По атлетам") {}

        @Composable
        private fun DownloadResultByCheckpointButton(modifier: Modifier) =
            DownloadResultsButton(modifier, "По контрольным пунктам") {}

    }


    private class WithResultTab(links: List<Hyperlink>, _modifier: Modifier) : TabLetka(links, _modifier) {
        override fun split(linksList: List<Hyperlink>): List<List<Hyperlink>> =
            linksList.groupBy { it.name[0] }.values.toList().reversed()
    }
}