package ru.emkn.kotlin.sms.view.table_view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.emkn.kotlin.sms.view.text_field.ITextField

enum class TableType {
    START_PROTOCOL,
    FINISH_PROTOCOL,
    APPLICATION,
    CHECKPOINT_RES,
}

abstract class TableView(
    list: List<List<String>>,
) {

    val columnsCount: Int

    open fun getFirstRow(list: List<List<String>>): List<ColumnInfo> = List(columnsCount) { ColumnInfo("", 100.dp) }
    open fun getOtherRows(list: List<List<String>>): MutableList<MutableList<MutableState<String>>> =
        list.map { sublist -> sublist.map { mutableStateOf(it) }.toMutableList() }.toMutableList()

    open val readOnly = false
    val firstRow: List<ColumnInfo>
    val rows: MutableList<MutableList<MutableState<String>>>
    val rowsCount: MutableState<Int>
    val isOpen = mutableStateOf(true)

    init {
        require(list.isNotEmpty()) { "Мне дали пустой лист" }
        columnsCount = list.first().size
        firstRow = getFirstRow(list)
        rows = getOtherRows(list)
        rowsCount = mutableStateOf(rows.size)
    }


    private val finalRows: List<List<String>>
        get() = getFinalRows(firstRow, rows)


    open class CellTextField(
        private val modifier: Modifier,
        private val str: String,
        override val readOnly: Boolean,
        private val onValueChange: (String) -> Unit,
    ) : ITextField {
        val HEIGHT = 50.dp

        @Composable
        override fun render() {
            TextField(
                modifier = modifier.height(HEIGHT),
                singleLine = true,
                value = str,
                onValueChange = onValueChange,
                readOnly = readOnly,
            )
        }
    }

    @Composable
    open fun drawTableRow(row: MutableList<MutableState<String>>) = repeat(row.size) { i ->
        CellTextField(Modifier.width(firstRow[i].width), row[i].value, true) { }.render()
    }

    @Composable
    open fun drawRow(number: Int, row: MutableList<MutableState<String>>, firstRow: List<ColumnInfo>) {
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
            drawTableRow(row)
        }
    }

    @Composable
    open fun drawFirstRow(row: List<ColumnInfo>) {
    }

    @Composable
    open fun drawLastRow() {
    }

    @Composable
    fun render() = MaterialTheme {
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            drawFirstRow(firstRow)
            repeat(rowsCount.value) { i -> drawRow(i, rows[i], firstRow) }
            drawLastRow()
        }
    }

    open fun getFinalRows(
        firstRow: List<ColumnInfo>,
        rows: MutableList<MutableList<MutableState<String>>>
    ): List<List<String>> = listOf(firstRow.map { it.name }) + rows.map { list -> list.map { it.value } }
}

val applicationFirstRow = listOf("Фамилия","Имя","Пол", "Г.р.",	"Разряд")
