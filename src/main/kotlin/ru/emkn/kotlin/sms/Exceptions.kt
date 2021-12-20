package ru.emkn.kotlin.sms

import ru.emkn.kotlin.sms.model.CompetitionSerialization
import ru.emkn.kotlin.sms.model.MetaInfo
import ru.emkn.kotlin.sms.model.application.TeamApplication
import ru.emkn.kotlin.sms.model.input_result.InputAthleteResults
import ru.emkn.kotlin.sms.model.input_result.InputCheckpointResults
import kotlin.String

class WeHaveAProblem(problem: String) : Exception(problem)

class InvalidDateFormat(date: String) : Exception("Файл содержит некорректный формат даты: $date")

class ApplicationCanNotBeRead(numberOfApplication: Int) :
    Exception("Заявку номер $numberOfApplication невозможно прочитать")

class ApplicationHasWrongFormat(numberOfApplication: Int) :
    Exception("Заявка номер $numberOfApplication неправильного формата. Ожидается:\n${TeamApplication.APPLICATION_FORMAT}")

class ApplicationHasWrongFormatOnLine(numberOfApplication: Int, line: String) :
    Exception("Заявка номер $numberOfApplication неправильного формата. Ожидается:\n${TeamApplication.APPLICATION_FORMAT}\n В реальности:\n$line")

class WrongSexInApplicationOnLine(numberOfApplication: Int, sex: String) :
    Exception("Заявка номер $numberOfApplication содержит спортсмена с неопределенным полом. Ожидается: М или Ж\n В реальности:\n$sex")

class WrongYearInApplicationOnLine(numberOfApplication: Int, year: String) :
    Exception("Заявка номер $numberOfApplication содержит спортсмена с неопределенным возрастом. Ожидается число.\n В реальности:\n$year")

class WrongCategoryInApplicationOnLine(numberOfApplication: Int, year: String) :
    Exception("Заявка номер $numberOfApplication содержит спортсмена с неопределенной категорией. Ожидается число.\n В реальности:\n$year")

class ResultMissesAthleteNumber(list: List<String>) :
    Exception("Отсутствует номер спортсмена в строке: $list")

class ResultByAthleteInvalidRow(line: List<String>) :
    Exception("Некорректная информация о прохождении. Ожидается \n${InputAthleteResults.ATHLETE_RESULT_FORMAT}\n В реальность\n $line")

class ResultMissesCheckPointName(list: List<String>) :
    Exception("В строке $list отсутствует название контрольного пункта на первой строке")

class ResultByCheckpointInvalidRow(line: List<String>) :
    Exception("Некорректная строка. Ожидается \n${InputCheckpointResults.CHECKPOINT_RESULT_FORMAT}\n В реальность\n $line")

class DirectoryCouldNotBeCreated(path: String) : Exception("Не получилось создать директорию $path")

class FileCouldNotBeCreated(fileName: String) : Exception("Не получилось создать файл $fileName")

class CompetitionDataTooFewArgumentsInRow(row: List<String>) :
    Exception("Файл [CompetitionData] поврежден: Ожидается\n${CompetitionSerialization.inputFormat}\nВ реальности\n$row")

class CompetitionDataInvalidSex(sex: String) : Exception("Некорректный пол $sex, введите М или Ж")

class CompetitionDataInvalidDate(date: String) : Exception("Некорректная дата $date")

class CompetitionDataInvalidSportCategory(sportCategory: String) :
    Exception("Некорректный спортивный разряд: $sportCategory")

class InputCheckpointResultIsAbsent(checkpointName: String) :
    Exception("Отсутствует результат на контрольном пункте $checkpointName")

class MetaInfoTooFewArguments(actual: Int) :
    Exception("Слишком мало аргументов для создания MetaInfo: ожидалось ${MetaInfo.Companion.Fields.values().size}, на самом деле $actual")

class InvalidSportType(sportType: String) : Exception("Некорректный вид спорта $sportType")

class InvalidCompetitionName(name: String) : Exception("Некорректное название соревнования $name")

class CompetitionAlreadyExist(name: String) : Exception("Соревнование с названием $name уже существует")

class FileDoNotDownload() : Exception("Загрузите файл с результатами")

class InvalidCSV(fileName: String) : Exception("$fileName не удовлевтворяет нужному формату (csv-таблица)")
