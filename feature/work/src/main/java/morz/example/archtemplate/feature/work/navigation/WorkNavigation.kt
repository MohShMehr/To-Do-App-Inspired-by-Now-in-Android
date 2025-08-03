package morz.example.archtemplate.feature.work.navigation

import morz.example.archtemplate.feature.work.WorkRoute
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val workNavigationRoute = "work_route"

fun NavController.navigateToWork(navOptions: NavOptions? = null){
    this.navigate(workNavigationRoute,navOptions)
}

fun NavGraphBuilder.workScreen(){
    composable(
        route = workNavigationRoute
    ) {
        WorkRoute()
    }
}