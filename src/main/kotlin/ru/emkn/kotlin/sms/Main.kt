package ru.emkn.kotlin.sms

import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.startprotocol.GroupStartProtocol
import ru.emkn.kotlin.sms.startprotocol.StartProtocol
import java.util.*

fun main(args: Array<String>) {

    val athlete1 = Athlete(
        Name("Миргалимова", "Розалина"),
        Sex.FEMALE,
        LocalDate.parse("2003-01-01"),
        Category("I"),
        GroupType("Amogus"),
        Team(TeamName("aboba"))
    )

    val athlete2 = Athlete(
        Name("Сибгатуллин", "Данил"),
        Sex.MALE,
        LocalDate.parse("2002-01-01"),
        Category("КМС"),
        GroupType("abobus"),
        Team(TeamName("aboba"))
    )

    val athlete3 = Athlete(
        Name("Москаленко", "Тимофей"),
        Sex.MALE,
        LocalDate.parse("2002-01-01"),
        Category("III"),
        GroupType("abobus"),
        Team(TeamName("aboba")),

    )
    val group1 = Group(GroupType("AMOGUS"), listOf(athlete1, athlete2, athlete3, athlete1))
    val group2 = Group(GroupType("Danil"), listOf(athlete2, athlete2, athlete2))

    StartProtocol(listOf(group1, group2), "Competition1")
}
