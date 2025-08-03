package morz.example.archtemplate.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import morz.example.archtemplate.R
import morz.example.archtemplate.core.designsystem.component.AppTopAppBar
import morz.example.archtemplate.feature.setting.navigation.settingNavigationRoute
import morz.example.archtemplate.feature.support.navigation.supportNavigationRoute
import morz.example.archtemplate.feature.tododetail.navigation.navigateToToDoDetail
import morz.example.archtemplate.feature.tododetail.navigation.toDoDetailNavigationRoute
import morz.example.archtemplate.ui.navigation.AppNavHost
import morz.example.archtemplate.ui.navigation.BottomNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    appState: AppState = rememberAppState()
) {

    Scaffold(
        topBar = {
            AppTopAppBar(
                navigationIcon = R.drawable.ic_nav_settings,
                navigationIconContentDescription = "App TopAppBar navigation icon bank login",
                actionIcon = R.drawable.ic_smart_support_filled,
                actionIconContentDescription = "App TopAppBar action icon avatar",
                onNavigationClick = {appState.navigateToRoute(settingNavigationRoute)},
                onActionClick = {appState.navigateToRoute(supportNavigationRoute)}
            )
        },
        bottomBar = {
            BottomNavBar(
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                currentDestination = appState.currentDestination,
                onAddClick = {appState.navController.navigateToToDoDetail()},
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppNavHost(appState = appState)
        }
    }
}
