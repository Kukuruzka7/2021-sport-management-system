package ru.emkn.kotlin.sms.view.table_view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.emkn.kotlin.sms.view.ColorScheme.ACCENT_C
import ru.emkn.kotlin.sms.view.ColorScheme.TEXT_C

//Тип таблицы
enum class TableType {
    START_PROTOCOL,
    FINISH_PROTOCOL,
    APPLICATION,
    CHECKPOINT_RES,
    ATHLETE_RES,
    COURSES,
    TEAM,
}

private val ROW_HEIGHT = 50.dp
private val BTN_HEIGHT = 50.dp
private val BTN_WIDTH = 50.dp

//Текстовое поле заголовка таблицы
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

//Сам заголовок
@Composable
private fun Header(row: List<ColumnInfo>) {
    repeat(row.size) { i -> HeaderTextField(Modifier.width(row[i].width).height(ROW_HEIGHT), row[i].name) }
}

//Кнопка добавить ряд
@Composable
private fun AddRowButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
        onClick = onClick,
    ) { Icon(Icons.Default.Add, contentDescription = null, tint = ACCENT_C) }
}

//Кнопка удалить ряд
@Composable
private fun DeleteRowButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
        onClick = onClick,
    ) { Icon(Icons.Default.Delete, contentDescription = null, tint = ACCENT_C) }
}

//Кнопка добавить колонку
@Composable
private fun AddColumnButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
        onClick = onClick,
    ) { Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = ACCENT_C) }
}

//Кнопка удалить колонку
@Composable
private fun DeleteColumnButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
        onClick = onClick,
    ) { Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null, tint = ACCENT_C) }
}

//Кнопка домой (хочу домой)
@Composable
private fun HomeButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH),
        onClick = onClick,
    ) { Icon(Icons.Default.Home, contentDescription = null, tint = ACCENT_C) }
}

//Рисовальщик ряда (в зависимости от изменяемости таблицы)
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

//Рисовальщик всей таблицы (внутренняя функция)
@Composable
private fun TableContent(
    modifier: Modifier,
    mutable: Boolean = false,//изменяемая
    drawHeader: Boolean = false,//с заголовком
    columnsAdder: Boolean = false,//добавление колонок
    columnsCnt: MutableState<Int> = mutableStateOf(1),
    firstRowFunc: (Int) -> List<ColumnInfo>,
    contentRows: MutableList<MutableList<MutableState<String>>>,
    saveBtnAction: () -> Unit,
) {
    val rowsCount = mutableStateOf(contentRows.size)

    fun firstRow() = firstRowFunc(columnsCnt.value)
    Column(modifier, Arrangement.spacedBy(5.dp)) {
        //Заголовок
        if (drawHeader) {
            Row(Modifier.wrapContentWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
                Header(firstRow())
                if (mutable) {
                    AddRowButton {
                        contentRows.add(0, MutableList(columnsCnt.value) { mutableStateOf("") })
                        rowsCount.value = contentRows.size
                    }
                }
                if (columnsAdder) {
                    DeleteColumnButton {
                        if (columnsCnt.value > 1) {
                            columnsCnt.value -= 2
                            contentRows.forEach {
                                it.removeLast()
                                it.removeLast()
                            }
                        }
                    }
                    AddColumnButton {
                        columnsCnt.value += 2
                        contentRows.forEach {
                            it.add(mutableStateOf(""))
                            it.add(mutableStateOf(""))
                        }
                    }
                }
                if (mutable) {
                    HomeButton {
                        saveBtnAction()
                    }
                }
            }
        }
        //все оставшиеся ряды
        repeat(rowsCount.value) { i ->
            Row(Modifier.wrapContentWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
                drawRow(contentRows[i], firstRow(), mutable)
                if (mutable) {
                    AddRowButton {
                        contentRows.add(i + 1, MutableList(columnsCnt.value) { mutableStateOf("") })
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

//Поля и функции отрисовки заголовка таблиц
private val applicationFirstRow = listOf("Фамилия", "Имя", "Пол", "Г.р.", "Разряд")
private val startProtocolFirstRow = listOf("Номер", "Фамилия", "Имя", "Г.р.", "Разряд", "Старт")
private val finishProtocolFirstRow =
    listOf("№ п/п", "Номер", "Фамилия", "Имя", "Г.р.", "Разр.", "Команда", "Результат", "Место", "Отставание")
private val teamFirstRow =
    listOf("Номер", "Фамилия Имя", "Пол", "Г.р.", "Разр.", "Команда", "Дистанция", "Пр. группа", "Старт")

private fun coursesFirstRow(n: Int): List<ColumnInfo> = List(n) { ColumnInfo("", 80.dp) }

private fun checkpointResFirstRow(n: Int): List<ColumnInfo> =
    listOf(ColumnInfo("", 80.dp, ::onlyDigitsFilter)) + List(n - 1) {
        when (it % 2) {
            0 -> {
                ColumnInfo("Атлет", 70.dp)
            }
            else -> {
                ColumnInfo("Время", 95.dp, ::timeFilter)
            }
        }
    }

private fun athletesResFirstRow(n: Int): List<ColumnInfo> =
    listOf(ColumnInfo("", 80.dp, ::onlyDigitsFilter)) + List(n - 1) {
        when (it % 2) {
            0 -> {
                ColumnInfo("Чекпойнт", 70.dp)
            }
            else -> {
                ColumnInfo("Время", 95.dp, ::timeFilter)
            }
        }
    }

//функция, которой пользоваться могут все
@Composable
fun TableContent(
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
                columnsCnt = mutableStateOf(10),
                firstRowFunc = { _: Int -> (finishProtocolFirstRow.map { it.toColumnType().getInfo(it) }) },
                contentRows = list.toMListMListStr()
            ) {}
        }
        TableType.START_PROTOCOL -> {
            TableContent(
                modifier = modifier,
                mutable = false,
                drawHeader = true,
                columnsCnt = mutableStateOf(6),
                firstRowFunc = { _: Int -> startProtocolFirstRow.map { it.toColumnType().getInfo(it) } },
                contentRows = list.toMListMListStr()
            ) {}
        }
        TableType.COURSES -> {
            TableContent(
                modifier = modifier,
                mutable = false,
                drawHeader = false,
                firstRowFunc = { coursesFirstRow(list.first().size) },
                contentRows = list.toMListMListStr()
            ) {}
        }
        TableType.TEAM -> {
            TableContent(
                modifier = modifier,
                mutable = false,
                drawHeader = true,
                columnsCnt = mutableStateOf(9),
                firstRowFunc = { _: Int -> teamFirstRow.map { it.toColumnType().getInfo(it) } },
                contentRows = list.toMListMListStr()
            ) {}
        }
        else -> throw Exception("Нужно вызвать вторую функцию TableContent")
    }
}

//функция, которой пользоваться могут все
@Composable
fun TableContent(
    type: TableType,
    modifier: Modifier,
    contentRows: MutableList<MutableList<MutableState<String>>>,
    columnsCnt: MutableState<Int> = mutableStateOf(1),
    saveBtnAction: () -> Unit = { },
) {
    when (type) {
        TableType.APPLICATION -> {
            TableContent(
                modifier = modifier,
                mutable = true,
                drawHeader = true,
                columnsCnt = mutableStateOf(5),
                firstRowFunc = { _: Int -> applicationFirstRow.map { it.toColumnType().getInfo(it) } },
                contentRows = contentRows
            ) { saveBtnAction() }
        }
        TableType.CHECKPOINT_RES -> {
            TableContent(
                modifier = modifier,
                mutable = true,
                drawHeader = true,
                columnsAdder = true,
                firstRowFunc = { i: Int -> checkpointResFirstRow(i) },
                columnsCnt = columnsCnt,
                contentRows = contentRows
            ) { saveBtnAction() }
        }
        TableType.ATHLETE_RES -> {
            TableContent(
                modifier = modifier,
                mutable = true,
                drawHeader = true,
                columnsAdder = true,
                firstRowFunc = { i: Int -> athletesResFirstRow(i) },
                columnsCnt = columnsCnt,
                contentRows = contentRows
            ) { saveBtnAction() }
        }
        else -> throw Exception("Нужно вызвать первую функцию TableContent")
    }
}

//MListMListStr -> ListListStr
fun MutableList<MutableList<MutableState<String>>>.toListListStr(): List<List<String>> =
    this.map { list -> list.map { it.value } }
//ListListStr -> MListMListStr
fun List<List<String>>.toMListMListStr(): MutableList<MutableList<MutableState<String>>> =
    this.map { list -> list.map { mutableStateOf(it) }.toMutableList() }.toMutableList()