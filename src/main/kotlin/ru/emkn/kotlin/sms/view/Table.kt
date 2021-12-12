package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class Table(val rows: MutableList<MutableList<MutableState<String>>>, val count: MutableState<Int>,) {
    val firstRow = listOf("Фамилия", "Имя")
    val names = listOf("Розалина", "Данил", "Тимофей")
    @Composable
    fun draw() = MaterialTheme {
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
                TextField(
                    modifier = Modifier.height(50.dp).width(300.dp),
                    singleLine = true,
                    value = firstRow[0],
                    onValueChange = { },
                    readOnly = true,
                )
                TextField(
                    modifier = Modifier.height(50.dp).width(300.dp),
                    singleLine = true,
                    value = firstRow[1],
                    onValueChange = { },
                    readOnly = true,
                )
            }
            repeat(count.value) { i ->
                Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
                    TextField(
                        modifier = Modifier.height(50.dp).width(300.dp),
                        singleLine = true,
                        value = rows[i][0].value,
                        onValueChange = { rows[i][0].value = it },
                    )
                    TextField(
                        modifier = Modifier.height(50.dp).width(300.dp),
                        singleLine = true,
                        value = rows[i][1].value,
                        onValueChange = { rows[i][1].value = it },
                    )
                }
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    rows.add(listOf(mutableStateOf(""), mutableStateOf(names.random())).toMutableList())
                    count.value = rows.size
                    println(rows.size)
                }) {
                Text(if (rows.size == 0) "Hello World" else "${rows.size}")
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    count.value = 0
                    rows.clear()
                }) {
                Text("Reset")
            }
        }
    }
}