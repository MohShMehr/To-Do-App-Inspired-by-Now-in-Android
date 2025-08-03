package morz.example.archtemplate.feature.login.navigation

import morz.example.archtemplate.feature.login.LoginRoute
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val loginNavigationRoute = "login_route"

fun NavController.navigateToLogin(navOptions: NavOptions? = null){
    this.navigate(loginNavigationRoute,navOptions)
}

fun NavGraphBuilder.loginScreen(){
    composable(
        route = loginNavigationRoute
    ) {
        LoginRoute()
    }
}