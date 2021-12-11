package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                state = rememberWindowState(width = 300.dp, height = 300.dp)
            ) {
                val count = remember { mutableStateOf(0) }
                val names = listOf("Розалина", "Данил", "Тимофей")
                var name = names.random()
                MaterialTheme {
                    var consumedText by remember { mutableStateOf(0) }
                    var text by remember { mutableStateOf("") }
                    Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                        TextField(
                            value = text,
                            onValueChange = { text = it },
                            )
                        TextField(
                            value = text,
                            onValueChange = { text = it },

                            )

                        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                consumedText++
                                count.value++
                                name = names.random()
                            }) {
                            Text(if (count.value == 0) "Hello World" else "${count.value}) $name")
                        }
                        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                count.value = 0
                            }) {
                            Text("Reset")
                        }
                    }
                    Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                        Text("Consumed text: $consumedText")
                        TextField(
                            value = text,
                            onValueChange = { text = it },
                            )
                    }
                }
            }
        }
    }
}