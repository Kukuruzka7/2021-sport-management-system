package ru.emkn.kotlin.sms.view.table_view

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

//Информация о конкретной колонке
data class ColumnInfo(val name: String, val width: Dp = 150.dp, val filter: (String) -> String = { it })

//Разные фильтры
fun onlyDigitsFilter(str: String): String = str.filter { ('0'..'9').contains(it) }
fun onlyLettersFilter(str: String): String =
    str.filter { (('a'..'z') + ('A'..'Z') + ('а'..'я') + ('А'..'Я') + '-').contains(it) }
fun onlyLettersAndSpaceFilter(str: String): String =
    str.filter { (('a'..'z') + ('A'..'Z') + ('а'..'я') + ('А'..'Я') + '-' + ' ').contains(it) }

fun timeFilter(str: String): String = str.filter { (('0'..'9') + ':').contains(it) }
fun delayFilter(str: String): String = str.filter { (('0'..'9') + ':' + '+').contains(it) }

//Каст ColumnType в ColumnInfo
fun ColumnType.getInfo(str: String): ColumnInfo = when (this) {
    ColumnType.AthleteNum -> ColumnInfo(str, 80.dp, ::onlyDigitsFilter)
    ColumnType.FirstName -> ColumnInfo(str, 170.dp, ::onlyLettersFilter)
    ColumnType.LastName -> ColumnInfo(str, 170.dp, ::onlyLettersFilter)
    ColumnType.FullName -> ColumnInfo(str, 210.dp, ::onlyLettersAndSpaceFilter)
    ColumnType.BirthYear -> ColumnInfo(str, 80.dp, ::onlyDigitsFilter)
    ColumnType.SportCategory -> ColumnInfo(str, 80.dp)
    ColumnType.StartTime -> ColumnInfo(str, 100.dp, ::timeFilter)
    ColumnType.Num -> ColumnInfo(str, 70.dp, ::onlyDigitsFilter)
    ColumnType.Team -> ColumnInfo(str, 200.dp)
    ColumnType.FinishResult -> ColumnInfo(str, 100.dp, ::timeFilter)
    ColumnType.Place -> ColumnInfo(str, 70.dp, ::onlyDigitsFilter)
    ColumnType.Delay -> ColumnInfo(str, 100.dp, ::delayFilter)
    ColumnType.Sex -> ColumnInfo(str, 70.dp, ::onlyLettersFilter)
    ColumnType.Race ->  ColumnInfo(str, 100.dp)
    ColumnType.PreferredGroup ->  ColumnInfo(str, 100.dp)
}

//Енум класс всех возможных типов колонок
enum class ColumnType {
    AthleteNum,
    FirstName,
    FullName,
    LastName,
    BirthYear,
    SportCategory,
    StartTime,

    Num,
    Team,
    FinishResult,
    Place,
    Delay,

    Sex,

    Race,
    PreferredGroup,
}

//Каст String в ColumnType
fun String.toColumnType() = when (this) {
    "Фамилия" -> ColumnType.LastName
    "Фамилия Имя" -> ColumnType.FullName
    "Имя" -> ColumnType.FirstName
    "Год рождения", "Г.р." -> ColumnType.BirthYear
    "Разряд", "Разр." -> ColumnType.SportCategory
    "Время старта", "Старт" -> ColumnType.StartTime

    "№ п/п" -> ColumnType.Num
    "Номер" -> ColumnType.AthleteNum
    "Команда" -> ColumnType.Team
    "Результат" -> ColumnType.FinishResult
    "Место" -> ColumnType.Place
    "Отставание" -> ColumnType.Delay

    "Пол" -> ColumnType.Sex

    "Дистанция" -> ColumnType.Race

    "Пр. группа" -> ColumnType.PreferredGroup

    else -> ColumnType.AthleteNum
}