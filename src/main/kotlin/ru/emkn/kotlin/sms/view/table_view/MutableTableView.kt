package ru.emkn.kotlin.sms.view.table_view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.emkn.kotlin.sms.view.button.IButton
import ru.emkn.kotlin.sms.view.button.IDeleteFileButton
import ru.emkn.kotlin.sms.view.button.ISaveButton

open class MutableTableView(list: List<List<String>>, type: TableType) : TableView(list), IMutableTableView {

    init {
        require(type == TableType.CHECKPOINT_RES || type == TableType.APPLICATION) {"MutableTableView не подходит для типа ${type}"}
    }

    constructor(columnsCount: Int, type: TableType) : this(List(columnsCount) { emptyList() }, type)

    protected class SaveButton(private val modifier: Modifier, override val onClick: () -> Unit) : ISaveButton {
        val WIDTH = 120.dp
        val HEIGHT = 50.dp
        override val text: String
            get() = "Save"

        @Composable
        override fun render() {
            Button(
                modifier = modifier.height(HEIGHT).width(WIDTH),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xF11BFF99)),
                shape = CircleShape,
                onClick = onClick
            ) {
                Text(text)
            }
        }
    }



    protected class DeleteRowButton(private val onClick: () -> Unit) : IDeleteFileButton {
        override val HEIGHT = 50.dp
        override val WIDTH = 50.dp

        override val text: String
            get() = "–"

        @Composable
        private fun drawIcon() = Icon(Icons.Default.Delete, contentDescription = null)

        @Composable
        override fun render() {
            Button(
                modifier = Modifier.height(HEIGHT).width(WIDTH),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xF11BFF99)),
                shape = CircleShape,
                onClick = onClick
            ) { drawIcon() }
        }
    }

    protected class AddRowButton(private val modifier: Modifier, private val onClick: () -> Unit) : IButton {
        val HEIGHT = 50.dp
        val WIDTH = 120.dp

        val text: String
            get() = "+"

        @Composable
        override fun render() {
            Button(
                modifier = modifier.height(HEIGHT).width(WIDTH),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xF11BFF99)),
                shape = CircleShape,
                onClick = onClick
            ) { Text(text) }
        }
    }


    @Composable
    override fun drawLastRow() {
        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            AddRowButton(Modifier.align(Alignment.TopStart)) {
                rows.add(MutableList(columnsCount) { mutableStateOf("") })
                rowsCount.value = rows.size
            }.render()
            SaveButton(Modifier.align(Alignment.TopEnd)) {
                isOpen.value = false
            }.render()
        }
    }


    @Composable
    override fun drawTableRow(row: MutableList<MutableState<String>>) {
        repeat(row.size) { i ->
            CellTextField(Modifier.width(firstRow[i].width), row[i].value) { str ->
                row[i].value = firstRow[i].filter(str)
            }.render()
        }
    }

    @Composable
    override fun drawRow(number: Int, row: MutableList<MutableState<String>>, firstRow: List<ColumnInfo>) {
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
            drawTableRow(row)
            DeleteRowButton {
                rows.removeAt(number)
                rowsCount.value = rows.size
            }.render()
        }
    }


}