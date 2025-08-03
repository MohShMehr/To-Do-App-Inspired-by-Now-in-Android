package morz.example.archtemplate.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import morz.example.archtemplate.R
import morz.example.archtemplate.core.designsystem.component.AppNavigationBar
import morz.example.archtemplate.core.designsystem.component.AppNavigationBarItem
import morz.example.archtemplate.core.designsystem.icon.AppIcons
import morz.example.archtemplate.feature.home.navigation.homeNavigationRoute
import morz.example.archtemplate.feature.login.navigation.loginNavigationRoute
import morz.example.archtemplate.feature.profile.navigation.profileNavigationRoute
import morz.example.archtemplate.feature.work.navigation.workNavigationRoute

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: @Composable () -> Unit
) {
    object Login : BottomNavItem(
        route = loginNavigationRoute,
        title = "Login",
        icon = {
            Icon(
                imageVector = AppIcons.Login,
                contentDescription = "icon profile"
            )
        }
    )

    object Tools : BottomNavItem(
        route = workNavigationRoute,
        title = "Tools",
        icon = {
            Icon(
                imageVector = AppIcons.Tools,
                contentDescription = "icon tools"
            )
        }
    )

    object Inbox : BottomNavItem(
        route = profileNavigationRoute,
        title = "Inbox",
        icon = {
            Icon(
                imageVector = AppIcons.Inbox,
                contentDescription = "icon inbox"
            )
        }
    )

    object Home : BottomNavItem(
        route = homeNavigationRoute,
        title = "Home",
        icon = {
            Icon(
                imageVector = AppIcons.Home,
                contentDescription = "icon home"
            )
        }
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Login,
    BottomNavItem.Tools,
    BottomNavItem.Inbox,
    BottomNavItem.Home
)

@Composable
fun BottomNavBar(
    onNavigateToDestination: (BottomNavItem) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
    onAddClick:()-> Unit
) {
    AppNavigationBar {
        bottomNavItems.forEachIndexed { index, item ->
            if (index == 2){
                Spacer(Modifier.weight(0.05f))
                AddButton(
                    modifier = modifier,
                    click = onAddClick
                )
                Spacer(Modifier.weight(0.05f))
            }
            val selected = currentDestination.isTopLevelDestinationInHierarchy(item)
            AppNavigationBarItem(
                selected = selected,
                onClick = {
                    onNavigateToDestination(item)
                },
                icon = item.icon,
                modifier = modifier.testTag(item::class.java.simpleName),
                label = { Text(text = item.title) },
                alwaysShowLabel = false
            )
        }
    }
}

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    click:()-> Unit
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clickable {click()}
    ) {
        Image(
            painter = painterResource(R.drawable.ic_nav_add),
            contentDescription = "Add new item",
            modifier = Modifier.fillMaxSize()
        )
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: BottomNavItem) =
    this?.hierarchy?.any {
        it.route?.contains(destination.route, true) ?: false
    } ?: false