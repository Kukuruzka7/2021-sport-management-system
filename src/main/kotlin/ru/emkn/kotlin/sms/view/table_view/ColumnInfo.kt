package ru.emkn.kotlin.sms.view.table_view

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ColumnInfo(val name: String, val width: Dp = 250.dp, val onlyDigits: Boolean = false)
