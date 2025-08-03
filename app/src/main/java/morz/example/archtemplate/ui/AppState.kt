package morz.example.archtemplate.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import morz.example.archtemplate.feature.home.navigation.navigateToHome
import morz.example.archtemplate.feature.login.navigation.navigateToLogin
import morz.example.archtemplate.feature.profile.navigation.navigateToProfile
import morz.example.archtemplate.feature.work.navigation.navigateToWork
import morz.example.archtemplate.ui.navigation.BottomNavItem

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController()
): AppState {
    return remember(
        navController
    ) {
        AppState(navController)
    }
}

@Stable
class AppState(
    val navController: NavHostController
) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    fun navigateToTopLevelDestination(topLevelDestination: BottomNavItem) {

        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            BottomNavItem.Login -> navController.navigateToLogin(topLevelNavOptions)
            BottomNavItem.Tools -> navController.navigateToWork(topLevelNavOptions)
            BottomNavItem.Home -> navController.navigateToHome(topLevelNavOptions)
            BottomNavItem.Inbox -> navController.navigateToProfile(topLevelNavOptions)
        }
    }

    fun navigateToRoute(
        navRoute: String,
        navOptions: NavOptions? = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }) {
        navController.navigate(navRoute, navOptions)
    }
}