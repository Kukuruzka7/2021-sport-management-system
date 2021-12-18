package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.emkn.kotlin.sms.model.Team
import ru.emkn.kotlin.sms.view.IModel

data class TeamHyperlink(val team: Team, val csvName: String)

class TeamTab(val teamLinks: List<TeamHyperlink>, _modifier: Modifier) : ITab(TabEnum.TEAMS, _modifier) {
    @Composable
    override fun render() {
        TODO("Not yet implemented")
    }
}