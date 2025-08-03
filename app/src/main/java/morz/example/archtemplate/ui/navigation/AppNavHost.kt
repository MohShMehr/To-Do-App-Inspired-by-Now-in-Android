package morz.example.archtemplate.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import morz.example.archtemplate.feature.home.navigation.homeNavigationRoute
import morz.example.archtemplate.feature.home.navigation.homeScreen
import morz.example.archtemplate.feature.login.navigation.loginScreen
import morz.example.archtemplate.feature.profile.navigation.profileScreen
import morz.example.archtemplate.feature.setting.navigation.settingScreen
import morz.example.archtemplate.feature.support.navigation.supportScreen
import morz.example.archtemplate.feature.tododetail.navigation.navigateToToDoDetail
import morz.example.archtemplate.feature.tododetail.navigation.toDoDetailNavigationRoute
import morz.example.archtemplate.feature.tododetail.navigation.toDoDetailScreen
import morz.example.archtemplate.feature.work.navigation.workScreen
import morz.example.archtemplate.ui.AppState

@Composable
fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
    startDestination: String = homeNavigationRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        homeScreen(
            onUpdateClick = navController::navigateToToDoDetail
        )
        loginScreen()
        workScreen()
        profileScreen()
        settingScreen()
        supportScreen()
        toDoDetailScreen()
    }
}