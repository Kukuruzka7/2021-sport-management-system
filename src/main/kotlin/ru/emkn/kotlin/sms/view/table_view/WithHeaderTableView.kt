package ru.emkn.kotlin.sms.view.table_view

import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.emkn.kotlin.sms.view.text_field.ITextField

class WithHeaderTableView(list: List<List<String>>, type: TableType) : TableView(list), IWithHeaderTableView {

    init {
        require(type == TableType.FINISH_PROTOCOL || type == TableType.START_PROTOCOL) { "WithHeaderTableView не подходит для типа ${type}" }
    }

    private class HeaderTextField(private val modifier: Modifier, private val str: String) : ITextField {
        val HEIGHT = 50.dp

        @Composable
        override fun render() {
            TextField(
                modifier = modifier.height(HEIGHT),
                singleLine = true,
                value = str,
                onValueChange = { },
                readOnly = true,
            )
        }
    }

    override fun getFirstRow(list: List<List<String>>): List<ColumnInfo> {
        return list.first().map {
            it.toColumnType().getInfo(it)
        }
    }

    override fun getOtherRows(list: List<List<String>>): MutableList<MutableList<MutableState<String>>> {
        return super.getOtherRows(list.drop(1))
    }

    @Composable
    override fun drawFirstRow(row: List<ColumnInfo>) {
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
            repeat(row.size) { i -> HeaderTextField(Modifier.width(row[i].width), row[i].name).render() }
        }
    }
}