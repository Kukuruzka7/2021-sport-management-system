package ru.emkn.kotlin.sms

class ApplicationCanNotBeRead(numberOfApplication: Int) :
    Exception("Заявку номер $numberOfApplication невозможно прочитать.") {
}

class ApplicationHasWrongFormat(numberOfApplication: Int) :
    Exception("Заявка номер $numberOfApplication не правильного формата. Ожидается:\n${applicationFormat}") {
}

class ApplicationHasWrongFormatOnLine(numberOfApplication: Int, line: String) :
    Exception("Заявка номер $numberOfApplication не правильного формата. Ожидается:\n${applicationFormat}\n В реальности:\n$line") {
}

class WrongSexInApplicationOnLine(numberOfApplication: Int, sex: String) :
    Exception("Заявка номер $numberOfApplication содержит спортсмена с неопределенным полом. Ожидается: М или Ж\n В реальности:\n$sex") {
}

class WrongYearInApplicationOnLine(numberOfApplication: Int, year: String) :
    Exception("Заявка номер $numberOfApplication содержит спортсмена с неопределенным возрастом. Ожидается число.\n В реальности:\n$year") {
}


