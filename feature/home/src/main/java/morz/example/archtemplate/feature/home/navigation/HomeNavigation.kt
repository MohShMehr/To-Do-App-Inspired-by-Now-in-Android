package morz.example.archtemplate.feature.home.navigation

import morz.example.archtemplate.feature.home.HomeRoute
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val homeNavigationRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null){
    this.navigate(homeNavigationRoute,navOptions)
}

fun NavGraphBuilder.homeScreen(
    onUpdateClick:(String)-> Unit
){
    composable(
        route = homeNavigationRoute
    ) {
        HomeRoute(
            onUpdateClick = onUpdateClick
        )
    }
}