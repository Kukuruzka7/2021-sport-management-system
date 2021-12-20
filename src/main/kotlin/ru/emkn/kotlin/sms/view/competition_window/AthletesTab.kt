package ru.emkn.kotlin.sms.view.competition_window

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.emkn.kotlin.sms.model.athlete.Athlete
import ru.emkn.kotlin.sms.view.ClickableTexxxt

class AthletesTab(private val athleteList: List<Athlete>, _modifier: Modifier) :
    TabLetka<Athlete>(_modifier, athleteList) {

    @Composable
    override fun toLink(element: Athlete) {
        ClickableTexxxt(Modifier, element.name.fullName, style) {}
    }

    override fun split(): List<List<Athlete>> =
        List(N_COLUMNS) { i -> athleteList.filterIndexed { index, _ -> index % N_COLUMNS == i } }

    companion object {
        private val N_COLUMNS = 3
    }

    @Composable
    override fun postrender() {
    }
}