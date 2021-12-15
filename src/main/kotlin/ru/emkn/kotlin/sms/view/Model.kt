package ru.emkn.kotlin.sms.view

import ru.emkn.kotlin.sms.model.Competition

interface IModel {
    val competition: Competition
}

class Model(override val competition: Competition) : IModel {

}
