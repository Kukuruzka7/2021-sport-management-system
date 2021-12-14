package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

data class ColumnInfo(val name: String, val width: Dp = 250.dp, val onlyDigits: Boolean = false)

class TableView(
    private val firstRow: List<ColumnInfo>,
    private val fileName: String,
) {
    private val columnsCount = firstRow.size
    private val rows: MutableList<MutableList<MutableState<String>>> =
        MutableList(0) { MutableList(0) { mutableStateOf("") } }
    private val rowsCount: MutableState<Int> = mutableStateOf(rows.size)

    @Composable
    private fun drawFirstRow(row: List<ColumnInfo>) {
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
            repeat(row.size) { i ->
                TextField(
                    modifier = Modifier.height(50.dp).width(row[i].width),
                    singleLine = true,
                    value = row[i].name,
                    onValueChange = { },
                    readOnly = true,
                )
            }
        }
    }

    @Composable
    private fun drawRow(number: Int, row: MutableList<MutableState<String>>, firstRow: List<ColumnInfo>) {
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
            repeat(row.size) { i ->
                TextField(
                    modifier = Modifier.height(50.dp).width(firstRow[i].width),
                    singleLine = true,
                    value = row[i].value,
                    onValueChange = { str ->
                        row[i].value = str.filter { ('0'..'9').contains(it) || !firstRow[i].onlyDigits }
                    },
                    readOnly = false,
                )
            }
            Button(
                modifier = Modifier.align(Alignment.CenterVertically).height(50.dp).width(50.dp),
                colors = buttonColors(backgroundColor = Color(0xF11BFF99)),
                shape = CircleShape,
                onClick = {
                    rows.removeAt(number)
                    rowsCount.value = rows.size
                }) {
                Text("—")
            }
        }
    }

    @Composable
    private fun drawLastRow() {
        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            Button(
                modifier = Modifier.align(Alignment.TopStart).height(50.dp).width(120.dp),
                colors = buttonColors(backgroundColor = Color(0xF11BFF99)),
                shape = CircleShape,
                onClick = {
                    rows.add(MutableList(columnsCount) { mutableStateOf("") })
                    rowsCount.value = rows.size
                }) {
                Text("+")
            }
            Button(
                modifier = Modifier.align(Alignment.TopEnd).height(50.dp).width(120.dp),
                colors = buttonColors(backgroundColor = Color(0xF11BFF99)),
                shape = CircleShape,
                onClick = { saveToFile(fileName, getFinalRows(firstRow, rows)) }
            ) {
                Text("Save")
            }
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
        csvWriter().open(fileName) {
            finalRows.forEach {
                writeRow(it)
            }
        }
    }
}