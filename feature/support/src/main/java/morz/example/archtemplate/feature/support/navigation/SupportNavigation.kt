package morz.example.archtemplate.feature.support.navigation

import morz.example.archtemplate.feature.support.SupportRoute
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val supportNavigationRoute = "support_route"

fun NavController.navigateToSupport(navOptions: NavOptions? = null){
    this.navigate(supportNavigationRoute,navOptions)
}

fun NavGraphBuilder.supportScreen(){
    composable(
        route = supportNavigationRoute
    ) {
        SupportRoute()
    }
}