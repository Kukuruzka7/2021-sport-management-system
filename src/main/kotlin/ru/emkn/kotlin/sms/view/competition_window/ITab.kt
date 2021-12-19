package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import ru.emkn.kotlin.sms.view.Model

enum class TabEnum {
    GROUPS, TEAMS, ATHLETES, START_PROTOCOLS, RESULT;
}

abstract class ITab(protected val modifier: Modifier) {
    @Composable
    abstract fun render()
}


/**
 * Это фабрика, производящая Табы. Она нужна для того, чтобы не создавать новые экземплеры ITab каждый раз, когда пользовательно открывает вкладку
 */
class TabFactory(private val model: Model) {
    private val map: MutableMap<TabEnum, ITab> = mutableMapOf()

    //Если не уверены, что Tab уже создан
    fun get(_tabEnum: MutableState<TabEnum>, modifier: Modifier): ITab {
        val tab = _tabEnum.value
        if (map[tab] != null) {
            return map[tab]!!
        }
        map[tab] = create(tab, modifier, model)
        return map[tab]!!
    }

    //Если уверены, что Tab уже создан
    fun get(_tabEnum: MutableState<TabEnum>): ITab = map[_tabEnum.value]!!


    private fun create(tabEnum: TabEnum, modifier: Modifier, model: Model): ITab = when (tabEnum) {
        TabEnum.GROUPS -> GroupsTab(
            model.competition.groupList.map { Hyperlink(it.race.groupName.value, {}) },
            modifier
        )
        TabEnum.TEAMS -> TeamsTab(
            model.competition.teamList.map { Hyperlink(it.name, {}) },
            modifier
        )
        TabEnum.ATHLETES -> AthletesTab(
            model.competition.athleteList.map { Hyperlink(it.name.fullName, {}) },
            modifier
        )
        TabEnum.START_PROTOCOLS -> StartProtocolsTab(modifier)
        TabEnum.RESULT -> ResultsTab(modifier)
    }
}
