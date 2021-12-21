package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import ru.emkn.kotlin.sms.Group
import ru.emkn.kotlin.sms.Model
import ru.emkn.kotlin.sms.readCSV
import ru.emkn.kotlin.sms.view.*
import ru.emkn.kotlin.sms.view.ColorScheme.GREY_C
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C
import ru.emkn.kotlin.sms.view.table_view.TableContent
import ru.emkn.kotlin.sms.view.table_view.TableType

interface ResultsTabManager : WindowManager {
    fun openResUplWindow(resultType: ResultType)
    fun closeCompWindow()
}

class ResultsTab(
    _modifier: Modifier,
    private val winManager: ResultsTabManager,
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
            Model.Companion.Stage.ONGOING -> NoResultTab(modifier, winManager).render()
        }
    }

    private class NoResultTab(private val modifier: Modifier, private val winManager: ResultsTabManager) {
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
            DownloadResultsButton(modifier, "По спортсменам") {
                winManager.openResUplWindow(ResultType.BY_ATHLETES)
                winManager.closeCompWindow()
            }

        @Composable
        private fun DownloadResultByCheckpointButton(modifier: Modifier) =
            DownloadResultsButton(modifier, "По пунктам") {
                winManager.openResUplWindow(ResultType.BY_ATHLETES)
                winManager.closeCompWindow()
            }
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
                    val horizontalState = rememberScrollState(0)
                    val verticalState = rememberScrollState(0)
                    Box(Modifier.fillMaxSize().background(color = ColorScheme.BACKGROUND_C).padding(DIALOG_PADDING)) {
                        TableContent(TableType.FINISH_PROTOCOL,
                            Modifier.align(Alignment.Center).verticalScroll(state = verticalState)
                                .horizontalScroll(horizontalState),
                            readCSV(fileNameBuilder(dialog.value!!)).drop(2)
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

        private companion object {
            val DIALOG_WIDTH = 750.dp
            val DIALOG_HEIGHT = 500.dp
            val DIALOG_PADDING = 20.dp
        }

        @Composable
        override fun toLink(element: Group) {
            ClickableTexxxt(Modifier, element.race.groupName, style) { dialog.value = element.race.groupName }
        }
    }
}