package ru.emkn.kotlin.sms.view.table_view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import ru.emkn.kotlin.sms.view.button.IButton
import ru.emkn.kotlin.sms.view.button.IDeleteFileButton
import ru.emkn.kotlin.sms.view.button.ISaveButton
import ru.emkn.kotlin.sms.view.text_field.ITextField

class TableView(
    private val firstRow: List<ColumnInfo>,
    private val fileName: String,
) {
    private val columnsCount = firstRow.size
    private val rows: MutableList<MutableList<MutableState<String>>> =
        MutableList(0) { MutableList(0) { mutableStateOf("") } }
    private val rowsCount: MutableState<Int> = mutableStateOf(rows.size)
    val isOpen = mutableStateOf(true)

    private class SaveButton(private val modifier: Modifier, override val onClick: () -> Unit) : ISaveButton {
        val WIDTH = 120.dp
        val HEIGHT = 50.dp
        override val text: String
            get() = "Save"

        @Composable
        override fun render() {
            Button(
                modifier = modifier.height(HEIGHT).width(WIDTH),
                colors = buttonColors(backgroundColor = Color(0xF11BFF99)),
                shape = CircleShape,
                onClick = onClick
            ) {
                Text(text)
            }
        }
    }

    private class DeleteFileButton(private val onClick: () -> Unit) : IDeleteFileButton {
        override val HEIGHT = 50.dp
        override val WIDTH = 50.dp

        override val text: String
            get() = "–"

        @Composable
        override fun render() {
            Button(
                modifier = Modifier.height(HEIGHT).width(WIDTH),
                colors = buttonColors(backgroundColor = Color(0xF11BFF99)),
                shape = CircleShape,
                onClick = onClick
            ) { Text(text) }
        }
    }

    private class AddRowButton(private val modifier: Modifier, private val onClick: () -> Unit) : IButton {
        val HEIGHT = 50.dp
        val WIDTH = 120.dp

        val text: String
            get() = "+"

        @Composable
        override fun render() {
            Button(
                modifier = modifier.height(HEIGHT).width(WIDTH),
                colors = buttonColors(backgroundColor = Color(0xF11BFF99)),
                shape = CircleShape,
                onClick = onClick
            ) { Text(text) }
        }
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

    private class CellTextField(
        private val modifier: Modifier,
        private val str: String,
        private val onValueChange: (String) -> Unit
    ) : ITextField {
        val HEIGHT = 50.dp

        @Composable
        override fun render() {
            TextField(
                modifier = modifier.height(HEIGHT),
                singleLine = true,
                value = str,
                onValueChange = onValueChange,
                readOnly = false,
            )
        }
    }

    @Composable
    private fun drawFirstRow(row: List<ColumnInfo>) {
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
            repeat(row.size) { i -> HeaderTextField(Modifier.width(row[i].width), row[i].name).render() }
        }
    }

    @Composable
    private fun drawRow(number: Int, row: MutableList<MutableState<String>>, firstRow: List<ColumnInfo>) {
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
            repeat(row.size) { i ->
                CellTextField(Modifier.width(firstRow[i].width), row[i].value) { str ->
                    row[i].value = str.filter { ('0'..'9').contains(it) || !firstRow[i].onlyDigits }
                }.render()
            }
            DeleteFileButton {
                rows.removeAt(number)
                rowsCount.value = rows.size
            }.render()
        }
    }

    @Composable
    private fun drawLastRow() {
        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            AddRowButton(Modifier.align(Alignment.TopStart)) {
                rows.add(MutableList(columnsCount) { mutableStateOf("") })
                rowsCount.value = rows.size
            }.render()
            SaveButton(Modifier.align(Alignment.TopEnd)) {
                saveToFile(fileName, getFinalRows(firstRow, rows))
            }.render()
        }
    }

    @Composable
    fun draw() = MaterialTheme {
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            drawFirstRow(firstRow)
            repeat(rowsCount.value) { i -> drawRow(i, rows[i], firstRow) }
            drawLastRow()
        }
    }

    private fun getFinalRows(
        firstRow: List<ColumnInfo>,
        rows: MutableList<MutableList<MutableState<String>>>
    ): List<List<String>> = listOf(firstRow.map { it.name }) + rows.map { list -> list.map { it.value } }


    private fun saveToFile(fileName: String, finalRows: List<List<String>>) {
        isOpen.value = false
    }
}