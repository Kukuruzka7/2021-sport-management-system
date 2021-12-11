package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

object View {
    fun render() {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Compose for Desktop",
                state = rememberWindowState(width = 1280.dp, height = 1080.dp)
            ) {
                val firstRow = listOf("Фамилия", "Имя")
                val rows = MutableList(0) { MutableList(0) { mutableStateOf("") } }
                val names = listOf("Розалина", "Данил", "Тимофей")
                val count = remember { mutableStateOf(rows.size) }
                MaterialTheme {
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
                                rows.add(listOf(mutableStateOf("") , mutableStateOf(names.random())).toMutableList())
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
        }
    }
}