package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import ru.emkn.kotlin.sms.Group
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
class TabFactory(private val model: Model, private val winManager: CompetitionWindowsManager) {
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


    private fun create(tabEnum: TabEnum, modifier: Modifier, model: Model): ITab {
        val competition = model.competition
        return when (tabEnum) {
            TabEnum.GROUPS -> {
                GroupsTab(buildRacesTable(competition.groupList), modifier)
            }
            TabEnum.TEAMS -> TeamsTab(competition.teamList, modifier)
            TabEnum.ATHLETES -> AthletesTab(competition.athleteList, modifier)
            TabEnum.START_PROTOCOLS -> StartProtocolsTab(competition.groupList, modifier)
            { model.getStartProtocolByGroupName(it) }
            TabEnum.RESULT -> ResultsTab(modifier, winManager, competition.groupList, model.stage)
            { groupName -> model.getFinishProtocolByGroupName(groupName) }
        }
    }

    fun buildRacesTable(groupList: List<Group>): List<List<String>> {
        val races = groupList.map {
            it.race.checkPoints.map { checkpoint -> checkpoint.name }.toMutableList()
                .apply { add(0, it.race.groupName) }
        }
        val maxLen = races.maxOf { it.size }
        return races.map { it + List(maxLen - it.size) { "" } }
    }
}
