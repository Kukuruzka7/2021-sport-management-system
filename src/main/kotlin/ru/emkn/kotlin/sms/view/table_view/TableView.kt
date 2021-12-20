package ru.emkn.kotlin.sms.view.table_view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.emkn.kotlin.sms.view.ColorScheme.ACCENT_C
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C

enum class TableType {
    START_PROTOCOL,
    FINISH_PROTOCOL,
    APPLICATION,
    CHECKPOINT_RES,
    COURSES,
}

private val ROW_HEIGHT = 50.dp
private val BTN_HEIGHT = 50.dp
private val BTN_WIDTH = 50.dp

@Composable
fun HeaderTextField(modifier: Modifier, str: String) {
    TextField(
        modifier = modifier,
        singleLine = true,
        value = str,
        onValueChange = { },
        readOnly = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = TEXT_C,
            focusedIndicatorColor = ACCENT_C,
            cursorColor = ACCENT_C
        ),
    )
}

@Composable
private fun Header(row: List<ColumnInfo>) {
    repeat(row.size) { i -> HeaderTextField(Modifier.width(row[i].width).height(ROW_HEIGHT), row[i].name) }
}

@Composable
private fun DeleteRowButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
        onClick = onClick,
    ) { Icon(Icons.Default.Delete, contentDescription = null, tint = ACCENT_C) }
}

@Composable
private fun AddRowButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
        onClick = onClick,
    ) { Icon(Icons.Default.Add, contentDescription = null, tint = ACCENT_C) }
}

@Composable
private fun HomeButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
        onClick = onClick,
    ) { Icon(Icons.Default.Home, contentDescription = null, tint = ACCENT_C) }
}

@Composable
private fun drawRow(list: MutableList<MutableState<String>>, firstRow: List<ColumnInfo>, mutable: Boolean) =
    repeat(list.size) { i ->
        if (mutable) {
            TextField(
                modifier = Modifier.width(firstRow[i].width).height(ROW_HEIGHT),
                singleLine = true,
                value = list[i].value,
                onValueChange = { list[i].value = firstRow[i].filter(it) },
                readOnly = false,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = TEXT_C,
                    focusedIndicatorColor = ACCENT_C,
                    cursorColor = ACCENT_C
                ),
            )
        } else {
            TextField(
                modifier = Modifier.width(firstRow[i].width).height(ROW_HEIGHT),
                singleLine = true,
                value = list[i].value,
                onValueChange = { },
                readOnly = true,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = TEXT_C,
                    focusedIndicatorColor = ACCENT_C,
                    cursorColor = ACCENT_C
                ),
            )
        }
    }

@Composable
fun TableContent(
    modifier: Modifier,
    mutable: Boolean = false,
    drawHeader: Boolean = false,
    firstRow: List<ColumnInfo> = emptyList(),
    contentRows: MutableList<MutableList<MutableState<String>>>,
    saveBtnAction: () -> Unit,
) {
    val rowsCount = mutableStateOf(contentRows.size)
    Column(modifier, Arrangement.spacedBy(5.dp)) {
        if (drawHeader) {
            Row(Modifier.wrapContentWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
                Header(firstRow)
                if (mutable) {
                    AddRowButton {
                        contentRows.add(0, MutableList(firstRow.size) { mutableStateOf("") })
                        rowsCount.value = contentRows.size
                    }
                    HomeButton {
                        saveBtnAction()
                    }
                }
            }
        }
        repeat(rowsCount.value) { i ->
            Row(Modifier.wrapContentWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
                drawRow(contentRows[i], firstRow, mutable)
                if (mutable) {
                    AddRowButton {
                        contentRows.add(i + 1, MutableList(firstRow.size) { mutableStateOf("") })
                        rowsCount.value = contentRows.size
                    }
                    DeleteRowButton {
                        contentRows.removeAt(i)
                        rowsCount.value = contentRows.size
                    }
                }
            }
        }
    }
}

val applicationFirstRow = listOf("Фамилия", "Имя", "Пол", "Г.р.", "Разряд")
val startProtocolFirstRow = listOf("Номер", "Фамилия", "Имя", "Год рождения", "Разряд", "Время старта")
val finishProtocolFirstRow =
    listOf("№ п/п", "Номер", "Фамилия", "Имя", "Г.р.", "Разр.", "Команда", "Результат", "Место", "Отставание")

fun checkpointResFirstRow(n: Int): List<ColumnInfo> =
    listOf(ColumnInfo("", 80.dp, ::onlyDigitsFilter)) + List(n - 1) {
        when (it % 2) {
            1 -> {
                ColumnInfo("", 95.dp, ::timeFilter)
            }
            else -> {
                ColumnInfo("", 70.dp)
            }
        }
    }

fun coursesFirstRow(n: Int): List<ColumnInfo> = List(n) { ColumnInfo("", 80.dp) }

@Composable
fun TableContentImmutable(
    type: TableType,
    modifier: Modifier,
    list: List<List<String>>,
) {
    when (type) {
        TableType.FINISH_PROTOCOL -> {
            TableContent(
                modifier = modifier,
                mutable = false,
                drawHeader = true,
                firstRow = finishProtocolFirstRow.map { it.toColumnType().getInfo(it) },
                contentRows = list.toMListMListStr()
            ) {}
        }
        TableType.START_PROTOCOL -> {
            TableContent(
                modifier = modifier,
                mutable = false,
                drawHeader = false,
                firstRow = startProtocolFirstRow.map { it.toColumnType().getInfo(it) },
                contentRows = list.toMListMListStr()
            ) {}
        }
        TableType.COURSES -> {
            TableContent(
                modifier = modifier,
                mutable = false,
                drawHeader = true,
                firstRow = coursesFirstRow(list.first().size),
                contentRows = list.toMListMListStr()
            ) {}
        }
        else -> throw Exception("Нужно вызвать функцию TableContent")
    }
}

@Composable
fun TableContent(
    type: TableType,
    modifier: Modifier,
    contentRows: MutableList<MutableList<MutableState<String>>>,
    saveBtnAction: () -> Unit = { },
) {
    when (type) {
        TableType.APPLICATION -> {
            TableContent(
                modifier = modifier,
                mutable = true,
                drawHeader = true,
                firstRow = applicationFirstRow.map { it.toColumnType().getInfo(it) },
                contentRows = contentRows
            ) { saveBtnAction() }
        }
        TableType.CHECKPOINT_RES -> {
            TableContent(
                modifier = modifier,
                mutable = true,
                drawHeader = false,
                firstRow = checkpointResFirstRow(contentRows.first().size),
                contentRows = contentRows
            ) { saveBtnAction() }
        }
        else -> throw Exception("Нужно вызвать функцию TableContentImmutable")
    }
}

fun MutableList<MutableList<MutableState<String>>>.toListListStr(): List<List<String>> =
    this.map { list -> list.map { it.value } }

fun List<List<String>>.toMListMListStr(): MutableList<MutableList<MutableState<String>>> =
    this.map { list -> list.map { mutableStateOf(it) }.toMutableList() }.toMutableList()