package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import ru.emkn.kotlin.sms.model.Team
import ru.emkn.kotlin.sms.model.athlete.toStringList
import ru.emkn.kotlin.sms.view.ClickableTexxxt
import ru.emkn.kotlin.sms.view.StartWindow

class TeamsTab(private val teamList: List<Team>, _modifier: Modifier) : TabLetka<Team>(_modifier, teamList) {
    private val dialog: MutableState<String?> = mutableStateOf(null)

    @Composable
    override fun postrender() {
        when (dialog.value) {
            null -> {}
            else -> Dialog(
                onCloseRequest = { dialog.value = null },
                title = dialog.value.toString(),
                state = rememberDialogState(
                    width = StartWindow.WIDTH, height = StartWindow.HEIGHT
                ),
            ) {}
        }
    }

    override fun split() =
        List(N_COLUMNS) { i -> teamList.filterIndexed { index, _ -> index % N_COLUMNS == i } }

    @Composable
    override fun toLink(element: Team) {
        ClickableTexxxt(modifier, element.name, style) { dialog.value = element.name }
    }


    private companion object {
        val N_COLUMNS = 2

        fun Team.toCSV(): List<List<String>> = this.athletes.map { it.toStringList() }
    }
}