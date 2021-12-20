package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import ru.emkn.kotlin.sms.Group
import ru.emkn.kotlin.sms.view.ClickableTexxxt
import ru.emkn.kotlin.sms.view.StartWindow

class StartProtocolsTab(private val groupList: List<Group>, _modifier: Modifier) :
    TabLetka<Group>(_modifier, groupList) {
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

    override fun split(): List<List<Group>> =
        groupList.groupBy { it.race.groupName[0] }.values.toList().reversed()

    @Composable
    override fun toLink(element: Group) {
        ClickableTexxxt(Modifier, element.race.groupName, style) { dialog.value = element.race.groupName }
    }

}