package ru.emkn.kotlin.sms.view.table_view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.emkn.kotlin.sms.view.ColorScheme
import ru.emkn.kotlin.sms.view.ColorScheme.ACCENT_C
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C

enum class TableType {
    START_PROTOCOL,
    FINISH_PROTOCOL,
    APPLICATION,
    CHECKPOINT_RES,
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
        textStyle = TextStyle(color = TEXT_C)
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
    open: MutableState<Boolean>,
    firstRow: List<ColumnInfo> = emptyList(),
    contentRows: MutableList<MutableList<MutableState<String>>>,
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
                        open.value = false
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

fun checkpointResFirstRow(n: Int): List<ColumnInfo> = List(n) { ColumnInfo("") }

@Composable
fun TableContent(
    type: TableType,
    modifier: Modifier,
    open: MutableState<Boolean> = mutableStateOf(true),
    contentRows: MutableList<MutableList<MutableState<String>>>,
) {
    when (type) {
        TableType.APPLICATION -> {
            TableContent(
                modifier = modifier,
                mutable = true,
                drawHeader = true,
                open = open,
                firstRow = applicationFirstRow.map { it.toColumnType().getInfo(it) },
                contentRows = contentRows
            )
        }
        TableType.FINISH_PROTOCOL -> {
            TableContent(
                modifier = modifier,
                mutable = false,
                drawHeader = true,
                open = open,
                firstRow = finishProtocolFirstRow.map { it.toColumnType().getInfo(it) },
                contentRows = contentRows
            )
        }
        TableType.START_PROTOCOL -> {
            TableContent(
                modifier = modifier,
                mutable = false,
                drawHeader = true,
                open = open,
                firstRow = startProtocolFirstRow.map { it.toColumnType().getInfo(it) },
                contentRows = contentRows
            )
        }
        TableType.CHECKPOINT_RES -> {
            TableContent(
                modifier = modifier,
                mutable = false,
                drawHeader = true,
                open = open,
                firstRow = checkpointResFirstRow(contentRows.first().size),
                contentRows = contentRows
            )
        }
    }
}

//abstract class TableView(
//    list: List<List<String>>,
//) {
//
//    val columnsCount: Int
//
//    open fun getFirstRow(list: List<List<String>>): List<ColumnInfo> = List(columnsCount) { ColumnInfo("", 100.dp) }
//    open fun getOtherRows(list: List<List<String>>): MutableList<MutableList<MutableState<String>>> =
//        list.map { sublist -> sublist.map { mutableStateOf(it) }.toMutableList() }.toMutableList()
//
//    open val readOnly = false
//    val firstRow: List<ColumnInfo>
//    val rows: MutableList<MutableList<MutableState<String>>>
//    val rowsCount: MutableState<Int>
//    val isOpen = mutableStateOf(true)
//
//    init {
//        require(list.isNotEmpty()) { "Мне дали пустой лист" }
//        columnsCount = list.first().size
//        firstRow = getFirstRow(list)
//        rows = getOtherRows(list)
//        rowsCount = mutableStateOf(rows.size)
//    }
//
//
//    private val finalRows: List<List<String>>
//        get() = getFinalRows(firstRow, rows)
//
//
//    open class CellTextField(
//        private val modifier: Modifier,
//        private val str: String,
//        override val readOnly: Boolean,
//        private val onValueChange: (String) -> Unit,
//    ) : ITextField {
//        val HEIGHT = 50.dp
//
//        @Composable
//        override fun render() {
//            TextField(
//                modifier = modifier.height(HEIGHT),
//                singleLine = true,
//                value = str,
//                onValueChange = onValueChange,
//                readOnly = readOnly,
//            )
//        }
//    }
//
//    @Composable
//    open fun drawTableRow(row: MutableList<MutableState<String>>) = repeat(row.size) { i ->
//        CellTextField(Modifier.width(firstRow[i].width), row[i].value, true) { }.render()
//    }
//
//    @Composable
//    open fun drawRow(number: Int, row: MutableList<MutableState<String>>, firstRow: List<ColumnInfo>) {
//        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
//            drawTableRow(row)
//        }
//    }
//
//    @Composable
//    open fun drawFirstRow(row: List<ColumnInfo>) {
//    }
//
//    @Composable
//    open fun drawLastRow() {
//    }
//
//    @Composable
//    fun render() = MaterialTheme {
//        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
//            drawFirstRow(firstRow)
//            repeat(rowsCount.value) { i -> drawRow(i, rows[i], firstRow) }
//            drawLastRow()
//        }
//    }
//
//    open fun getFinalRows(
//        firstRow: List<ColumnInfo>,
//        rows: MutableList<MutableList<MutableState<String>>>
//    ): List<List<String>> = listOf(firstRow.map { it.name }) + rows.map { list -> list.map { it.value } }
//}
//
