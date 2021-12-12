package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import ru.emkn.kotlin.sms.application.Application
import java.io.File

var files: MutableList<File> = mutableListOf()

object View {
    fun render() {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Upload team's applications",
                state = WindowState(width = 1500.dp, height = 1000.dp)
            ) {
                Column(
                    Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)
                ) {
                    val count = mutableStateOf(files.size)
                    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
                        Button(
                            onClick = {
                                val n = java.awt.FileDialog(ComposeWindow())
                                n.isMultipleMode = true
                                n.isVisible = true
                                files += n.files
                                count.value = files.size
                            }
                        ) {
                            Text("Загрузить файл.")
                        }

                        Button(
                            onClick = {
                                Application(files) // надоо ловить ексепшонсы
                                //переход в мейн надо вернуть Application
                            }
                        ) {
                            Text("Сохранить заявки.")
                        }
                    }
                    for (i in 0 until count.value) {
                        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp), Alignment.Top) {
                            FileButton(files[i].name, index = i)
                            Button(
                                modifier = Modifier.align(Alignment.CenterVertically).height(50.dp).width(50.dp),
                                colors = buttonColors(backgroundColor = Color(0xF11BFF99)),
                                shape = CircleShape,
                                onClick = {
                                    files.removeAt(i)
                                    count.value--
                                }) {
                                Text("—")
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FileButton(
    text: String = "",
    index: Int
) {
    val interactionSource = remember { MutableInteractionSource() }
    Button(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .size(500.dp, 50.dp)
            .focusable(interactionSource = interactionSource),
        onClick = { println(files[index]) }
    ) {
        Text(text = text, color = Color.White)
    }
}
