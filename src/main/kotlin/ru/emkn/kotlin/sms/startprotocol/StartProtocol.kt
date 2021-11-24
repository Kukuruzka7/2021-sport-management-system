package ru.emkn.kotlin.sms.startprotocol

import ru.emkn.kotlin.sms.DirectoryCouldNotBeCreated
import ru.emkn.kotlin.sms.Group
import java.io.File

class StartProtocol(listGroup: List<Group>, name: String) {
    private val generateCSV: List<GroupStartProtocol>
    private val path: String

    companion object {
        const val dir = "src/main/resources/competitions/"
    }

    init {
        path = "$dir$name"
        // Нам рассказывали, что с фалом могут произойти какие-то беды, поэтому нужно проверять прям на месте
        if (!File(path).exists()) {
            try {
                File(path).mkdir()
            } catch (_: Exception) {
                throw DirectoryCouldNotBeCreated(path)
            }
        }

        generateCSV = listGroup.map { GroupStartProtocol(it, path) }
        generateCSV.forEach { it.toCSV }
    }
}
