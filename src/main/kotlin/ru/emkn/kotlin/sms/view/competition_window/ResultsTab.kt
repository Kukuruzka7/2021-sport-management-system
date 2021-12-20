package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import ru.emkn.kotlin.sms.Group
import ru.emkn.kotlin.sms.readCSV
import ru.emkn.kotlin.sms.view.ColorScheme
import ru.emkn.kotlin.sms.view.ColorScheme.GREY_C
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C
import ru.emkn.kotlin.sms.view.Model
import ru.emkn.kotlin.sms.view.table_view.TableContent
import ru.emkn.kotlin.sms.view.table_view.TableType

class ResultsTab(
    _modifier: Modifier,
    private val groupList: List<Group>,
    private val stage: MutableState<Model.Companion.Stage>,
    private val fileNameBuilder: (String) -> String
) :
    ITab(_modifier) {

    @Composable
    override fun render() {
        when (stage.value) {
            Model.Companion.Stage.FINISHED -> WithResultTab(
                groupList, modifier, fileNameBuilder
            ).render()
            Model.Companion.Stage.ONGOING -> NoResultTab(modifier).render()
        }
    }

    private class NoResultTab(private val modifier: Modifier) {
        @Composable
        fun render() {
            Box(modifier = modifier.padding(PADDING).fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(SPACING)
                ) {
                    Text(
                        text = NO_RESULT_TEXT, fontSize = NO_RESULT_FONT_SIZE, color = TEXT_C
                    )
                    Text(
                        text = SUGGESTION_TEXT, fontSize = SUGGESTION_FONT_SIZE, color = TEXT_C
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        DownloadResultByAthletesButton(Modifier)
                        DownloadResultByCheckpointButton(Modifier)
                    }
                }
            }
        }

        private companion object {
            const val NO_RESULT_TEXT = "Результаты пока не были загружены в систему"
            val NO_RESULT_FONT_SIZE = 16.sp
            val PADDING = 15.dp
            const val SUGGESTION_TEXT = "Вы можете сделать это сейчас:"
            val SUGGESTION_FONT_SIZE = 14.sp
            val SPACING = 20.dp
        }

        @Composable
        private fun DownloadResultsButton(modifier: Modifier, text: String, onClick: () -> Unit) {
            Button(
                modifier = modifier, onClick = onClick, colors = ButtonDefaults.buttonColors(backgroundColor = GREY_C)
            ) {
                Text(text = text, color = TEXT_C)
            }

        }

        @Composable
        private fun DownloadResultByAthletesButton(modifier: Modifier) =
            DownloadResultsButton(modifier, "По спортсменам") {}

        @Composable
        private fun DownloadResultByCheckpointButton(modifier: Modifier) =
            DownloadResultsButton(modifier, "По пунктам") {}

    }


    private class WithResultTab(
        val groupList: List<Group>,
        _modifier: Modifier,
        private val fileNameBuilder: (String) -> String
    ) :
        TabLetka<Group>(_modifier, groupList) {
        private val dialog: MutableState<String?> = mutableStateOf(null)
        override fun split(): List<List<Group>> = groupList.groupBy { it.race.groupName[0] }.values.toList().reversed()


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
                    Box(Modifier.fillMaxSize().background(color = ColorScheme.BACKGROUND_C).padding(DIALOG_PADDING)) {
                        TableContent(TableType.FINISH_PROTOCOL, Modifier, readCSV(fileNameBuilder(dialog.value!!)))
                    }
                }
            }
        }

        private companion object {
            val DIALOG_WIDTH = 750.dp
            val DIALOG_HEIGHT = 500.dp
            val DIALOG_PADDING = 20.dp
        }

        @Composable
        override fun toLink(element: Group) {
            val annotatedText = buildAnnotatedString {
                //append your initial text
                withStyle(
                    style = style
                ) {
                    append(element.race.groupName)
                }
            }
            ClickableText(modifier = modifier, text = annotatedText, onClick = { dialog.value })
        }
    }
}