package morz.example.archtemplate.feature.tododetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import morz.example.archtemplate.feature.tododetail.ToDoDetailRoute

private const val TODO_ARG_NAME = "toDoId"
const val toDoDetailNavigationRoute = "toDoDetail_route"

fun NavController.navigateToToDoDetail(toDoId: String = "0") {
    this.navigate("$toDoDetailNavigationRoute/$toDoId"){
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.toDoDetailScreen() {
    composable(
        route = "$toDoDetailNavigationRoute/{${TODO_ARG_NAME}}",
        arguments = listOf(
            navArgument(TODO_ARG_NAME) { type = NavType.StringType },
        )
    ) { backStackEntry ->
        val toDoId = backStackEntry.arguments?.getString(TODO_ARG_NAME) ?: ""
        ToDoDetailRoute(toDoId)
    }
}