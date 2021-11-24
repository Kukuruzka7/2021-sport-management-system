package ru.emkn.kotlin.sms

import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.startprotocol.GroupStartProtocol
import ru.emkn.kotlin.sms.startprotocol.StartProtocol
import java.util.*

fun main(args: Array<String>) {
    var birthDate = GregorianCalendar()

    birthDate.set(2003, 10, 11, 0, 0, 0)

    val athlete1 = Athlete(
        Name("Миргалимова", "Розалина"),
        Sex.FEMALE,
        null,
        Category("I"),
        Race("aboba"),
        Team(TeamName("amogus"))
    )
    birthDate.set(2002, 12, 31, 0, 0, 0);
    val athlete2 = Athlete(
        Name("Сибгатуллин", "Данил"),
        Sex.MALE,
        null,
        Category("КМС"),
        Race("aboba"),
        Team(TeamName("amogus"))
    )
    birthDate.set(2004, 3, 24, 0, 0, 0);
    val athlete3 = Athlete(
        Name("Москаленко", "Тимофей"),
        Sex.MALE,
        null,
        Category("III"),
        Race("aboba"),
        Team(TeamName("amogus"))
    )
    val group1 = Group(Race("aboba"))
    group1.athletes =  listOf(athlete1, athlete2, athlete3, athlete1)
    val group2 = Group(Race("Даниl"))
    group2.athletes =   listOf(athlete2, athlete2, athlete2)

    StartProtocol(listOf(group1, group2), "Competition1")
}
