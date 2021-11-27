package ru.emkn.kotlin.sms

import ru.emkn.kotlin.sms.input_result.InputAthleteResults
import ru.emkn.kotlin.sms.input_result.InputCheckpointResults
import ru.emkn.kotlin.sms.result_data.Checkpoint

class WeHaveAProblem(problem: String) : Exception(problem)

class InvalidDateFormatInFile(fileName: String, date: String) :
    Exception("В файле $fileName некорректный формат даты: $date")

class InvalidDateFormat(date: String) : Exception("Некорректный формат даты: $date")

class ApplicationCanNotBeRead(numberOfApplication: Int) :
    Exception("Заявку номер $numberOfApplication невозможно прочитать.")

class ApplicationHasWrongFormat(numberOfApplication: Int) :
    Exception("Заявка номер $numberOfApplication неправильного формата. Ожидается:\n${TeamApplication.APPLICATION_FORMAT}")


class ApplicationHasWrongFormatOnLine(numberOfApplication: Int, line: String) :
    Exception("Заявка номер $numberOfApplication неправильного формата. Ожидается:\n${TeamApplication.APPLICATION_FORMAT}\n В реальности:\n$line")

class WrongSexInApplicationOnLine(numberOfApplication: Int, sex: String) :
    Exception("Заявка номер $numberOfApplication содержит спортсмена с неопределенным полом. Ожидается: М или Ж\n В реальности:\n$sex")

class WrongYearInApplicationOnLine(numberOfApplication: Int, year: String) :
    Exception("Заявка номер $numberOfApplication содержит спортсмена с неопределенным возрастом. Ожидается число.\n В реальности:\n$year")

class WrongAthleteNameInResults(athleteName: Name) : Exception("Спортсмен с именем ${athleteName.fullName}")

class ResultCanNotBeRead(fileName: String) : Exception("Результат в файле $fileName не может быть прочитан")

class ResultMissesAthleteNumber(list: List<String>) :
    Exception("Отсутствует номер спортсмена на первой строке: $list")

class ResultByAthleteInvalidRow(line: List<String>) :
    Exception("Некорректная информация от прохожднии. Ожидается \n${InputAthleteResults.ATHLETE_RESULT_FORMAT}\n В реальность\n $line")

class ResultMissesCheckPointName(list: List<String>) :
    Exception("В строке $list отсутствует название контрольного пункта на первой строке")

class ResultByCheckpointInvalidRow(line: List<String>) :
    Exception("Некорректная строка. Ожидается \n${InputCheckpointResults.CHECKPOINT_RESULT_FORMAT}\n В реальность\n $line")

class DirectoryCouldNotBeCreated(path: String) : Exception("Не получилось создать директорию $path")

class FileCouldNotBeCreated(fileName: String) : Exception("Не получилось создать файл $fileName")

class CompetitionDataTooFewArgumentsInRow(row: List<String>) :
    Exception("Файл [CompetitionData] поврежден: Ожидается\n${CompetitionData.inputFormat}\nВ реальности\n$row")

class CompetitionDataInvalidSex(sex: String) : Exception("Некорректный пол $sex, введите М или Ж")

class CompetitionDataInvalidDate(date: String) : Exception("Некорректная дата $date")

class CompetitionDataInvalidSportCategory(sportCategory: String) :
    Exception("Некорректный спортивный разряд: $sportCategory")

class InputCheckpointResultIsAbsent(checkpoint: Checkpoint) :
    Exception("Отсутствует результат на контрольном пункте $checkpoint")

