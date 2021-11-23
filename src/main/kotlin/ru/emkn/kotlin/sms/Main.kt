package ru.emkn.kotlin.sms

import java.util.*

fun main(args: Array<String>) {
    var birthDate = GregorianCalendar()

    birthDate.set(2003, 10, 11, 0, 0, 0);

    val athlete1 = Athlete(
        AthleteNumber("123"),
        Name("Миргалимова", "Розалина"),
        Sex.FEMALE,
        birthDate.time,
        Category("I"),
        Insurance(true),
        MedicalCondition(true),
        TODO()
    )
}
