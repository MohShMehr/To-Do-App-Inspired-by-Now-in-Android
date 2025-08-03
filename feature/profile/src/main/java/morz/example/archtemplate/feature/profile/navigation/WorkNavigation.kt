package morz.example.archtemplate.feature.profile.navigation

import morz.example.archtemplate.feature.profile.ProfileRoute
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val profileNavigationRoute = "profile_route"

fun NavController.navigateToProfile(navOptions: NavOptions? = null){
    this.navigate(profileNavigationRoute,navOptions)
}

fun NavGraphBuilder.profileScreen(){
    composable(
        route = profileNavigationRoute
    ) {
        ProfileRoute()
    }
}