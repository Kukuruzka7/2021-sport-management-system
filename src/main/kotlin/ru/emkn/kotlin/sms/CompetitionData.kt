package ru.emkn.kotlin.sms

class AthleteData

class CompetitionData(val athletesData: List<List<String>>) {
    fun save(fileName: String) {
        Fields.NAME.ordinal
    }

    companion object {
        enum class Fields {
            NUMBER, NAME, BIRTH_DATE, SPORT_CATEGORY, PREFERRED_GROUP, GROUP, TEAM
        }
    }
}