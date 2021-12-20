package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.emkn.kotlin.sms.view.table_view.TableContent
import ru.emkn.kotlin.sms.view.table_view.TableContentImmutable
import ru.emkn.kotlin.sms.view.table_view.TableType
import ru.emkn.kotlin.sms.view.table_view.toMListMListStr

class GroupsTab(val races: List<List<String>>, _modifier: Modifier) : ITab(_modifier) {
    @Composable
    override fun render() {
        TableContentImmutable(TableType.COURSES, modifier, races)
    }
}