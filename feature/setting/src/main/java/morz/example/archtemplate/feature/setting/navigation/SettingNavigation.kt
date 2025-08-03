package morz.example.archtemplate.feature.setting.navigation

import morz.example.archtemplate.feature.setting.SettingRoute
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val settingNavigationRoute = "setting_route"

fun NavController.navigateToSetting(navOptions: NavOptions? = null){
    this.navigate(settingNavigationRoute,navOptions)
}

fun NavGraphBuilder.settingScreen(){
    composable(
        route = settingNavigationRoute
    ) {
        SettingRoute()
    }
}