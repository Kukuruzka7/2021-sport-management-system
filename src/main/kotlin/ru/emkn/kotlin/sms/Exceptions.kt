package ru.emkn.kotlin.sms

class InvalidDateFormat(fileName: String, date: String) : Exception("В файле $fileName некорректный формат даты: $date")

class ApplicationCanNotBeRead(numberOfApplication: Int) :
    Exception("Заявку номер $numberOfApplication невозможно прочитать.")

class ApplicationHasWrongFormat(numberOfApplication: Int) :
    Exception("Заявка номер $numberOfApplication неправильного формата. Ожидается:\n${applicationFormat}")


class ApplicationHasWrongFormatOnLine(numberOfApplication: Int, line: String) :
    Exception("Заявка номер $numberOfApplication неправильного формата. Ожидается:\n${applicationFormat}\n В реальности:\n$line")

class WrongSexInApplicationOnLine(numberOfApplication: Int, sex: String) :
    Exception("Заявка номер $numberOfApplication содержит спортсмена с неопределенным полом. Ожидается: М или Ж\n В реальности:\n$sex")

class WrongYearInApplicationOnLine(numberOfApplication: Int, year: String) :
    Exception("Заявка номер $numberOfApplication содержит спортсмена с неопределенным возрастом. Ожидается число.\n В реальности:\n$year")

class WrongAthleteNameInResults(athleteName: Name) : Exception("Спортсмен с именем ${athleteName.fullName}")

class ResultCanNotBeRead(fileName: String) : Exception("Результат в файле $fileName не может быть прочитан")

class ResultMissesAthleteNumber(fileName: String) :
    Exception("В файле $fileName отсутствует номер спортсмена на первой строке")

class ResultByAthleteInvalidRow(fileName: String, line: List<String>) :
    Exception("Файл $fileName содержит некорректную строку. Ожидается \n${InputAthleteResults.ATHLETE_RESULT_FORMAT}\n В реальность\n $line")

class ResultMissesCheckPointName(fileName: String) :
    Exception("В файле $fileName отсутствует название контрольного пункта на первой строке")

class ResultByCheckpointInvalidRow(fileName: String, line: List<String>) :
    Exception("Файл $fileName содержит некорректную строку. Ожидается \n${InputCheckpointResults.CHECKPOINT_RESULT_FORMAT}\n В реальность\n $line")