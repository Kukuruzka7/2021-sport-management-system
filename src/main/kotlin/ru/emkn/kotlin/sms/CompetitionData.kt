package ru.emkn.kotlin.sms
class AthleteData
//Номер,Имя,Пол,ГодРождения,Разряд, ПредпочтительнаяГруппа
class CompetitionData (val athletesData: List<List<String>>) {
    fun save(fileName: String) {
        TODO()
    }

    companion object {
        enum class Fields {
            NUMBER, NAME, BIRTH_DATE, SPORT_CATEGORY, PREFERRED_GROUP, GROUP, TEAM
        }
    }
}