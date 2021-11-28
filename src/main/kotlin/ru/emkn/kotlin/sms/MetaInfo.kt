package ru.emkn.kotlin.sms
import kotlinx.datetime.LocalDate
import java.io.File

data class MetaInfo(val name: String, val date: LocalDate, val sport: SportType) {
    override fun toString() = "[$name, $date, $sport]"

    constructor(metaInfo: MetaInfo) : this(metaInfo.name, metaInfo.date, metaInfo.sport)

    constructor(args: List<String>) : this(parse(args))

    fun toStringList(): List<String> {
        return Fields.values().map { extractFieldToString(it) }
    }

    private fun extractFieldToString(field: Fields): String = when (field) {
        Fields.NAME -> name
        Fields.DATE -> date.toString()
        Fields.SPORT_TYPE -> sport.toString()
    }

    companion object {
        fun parse(args: List<String>): MetaInfo {
            check(args)
            return MetaInfo(
                args[Fields.NAME.ordinal],
                LocalDate.parse(args[Fields.DATE.ordinal]),
                SportType.getSportType(args[Fields.SPORT_TYPE.ordinal])
            )
        }

        fun check(args: List<String>) {
            if (args.size < Fields.values().size) {
                throw MetaInfoTooFewArguments(args.size)
            }
            try {
                LocalDate.parse(args[Fields.DATE.ordinal])
            } catch (_: Exception) {
                throw InvalidDateFormat(args[Fields.DATE.ordinal])
            }
            if (SportType.getSportType(args[Fields.SPORT_TYPE.ordinal]) == SportType.X) {
                throw InvalidSportType(args[Fields.SPORT_TYPE.ordinal])
            }
        }

        enum class Fields {
            NAME, DATE, SPORT_TYPE
        }
    }
}


