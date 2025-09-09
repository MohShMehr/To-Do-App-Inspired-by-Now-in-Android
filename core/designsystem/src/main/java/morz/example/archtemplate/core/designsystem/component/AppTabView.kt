package morz.example.archtemplate.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

class TabsViewState(selectedIndex: Int = 0) {
    var selectedTabIndex by mutableIntStateOf(selectedIndex)
        private set

    fun updateSelected(index: Int) {
        selectedTabIndex = index
    }

    companion object {
        val Saver: Saver<TabsViewState, *> = listSaver(
            save = { listOf(it.selectedTabIndex) },
            restore = { TabsViewState(it.first()) }
        )
    }
}

@Composable
fun rememberTabsViewState(selectedIndex: Int): TabsViewState =
    rememberSaveable(saver = TabsViewState.Saver) { TabsViewState(selectedIndex) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabsView(
    modifier: Modifier = Modifier,
    state: TabsViewState = rememberTabsViewState(0),
    titles: List<String>,
    containerColor: Color = TabRowDefaults.primaryContainerColor,
    contentColor: Color = TabRowDefaults.primaryContentColor,
    indicator: @Composable TabIndicatorScope.() -> Unit = {
        TabRowDefaults.PrimaryIndicator(
            modifier = Modifier.tabIndicatorOffset(state.selectedTabIndex, matchContentSize = true),
            width = Dp.Unspecified,
        )
    },
    divider: @Composable () -> Unit = @Composable { HorizontalDivider() },
    tabText: @Composable (() -> Unit)? = null,
    tabIcon: @Composable (() -> Unit)? = null,
) {

    Column {
        PrimaryTabRow(
            modifier = modifier,
            containerColor = containerColor,
            contentColor = contentColor,
            indicator = indicator,
            divider = divider,
            selectedTabIndex = state.selectedTabIndex) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state.selectedTabIndex == index,
                    onClick = { state.updateSelected(index) },
                    text = tabText ?: {
                        Text(title)
                    },
                    icon = tabIcon
                )
            }
        }
    }
}
