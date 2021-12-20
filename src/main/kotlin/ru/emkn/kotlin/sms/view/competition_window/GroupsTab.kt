package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.emkn.kotlin.sms.view.table_view.TableContent
import ru.emkn.kotlin.sms.view.table_view.TableType

class GroupsTab(val races: List<List<String>>, _modifier: Modifier) : ITab(_modifier) {
    @Composable
    override fun render() {
        TableContent(TableType.COURSES, modifier, races)
    }
}