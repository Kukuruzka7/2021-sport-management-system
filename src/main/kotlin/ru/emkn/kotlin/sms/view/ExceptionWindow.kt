package ru.emkn.kotlin.sms.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState


//interface ExceptionWindow : IWindow {
//    override val state: MutableState<Boolean> = mutableStateOf(false)
//    override fun render()
//}

//class ApplicationExceptionWindow(val e: Exception) : ExceptionWindow {

//    override fun render() {
//        Dialog(
//            onCloseRequest = { openingApplicationException.value = -1 },
//            title = "Ошибка",
//            state = rememberDialogState(width = 200.dp, height = 200.dp)
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(
//                    e.message
//                        ?: "Во время инициализации программы произошла сурьезная ошибка.\n Либо не хватает памяти, либо не читается диск, либо вы стерли из каталога пару важных файлов.\n Короче говоря не судьба."
//                )
//                Button(
//                    onClick = { openingApplicationException.value = -1 }
//                ) {
//                    Text("Изменить заявления.")
//                }
//            }
//        }
//    }
//}
