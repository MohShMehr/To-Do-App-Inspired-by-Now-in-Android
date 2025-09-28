package morz.example.archtemplate.feature.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import morz.example.archtemplate.core.designsystem.component.AppBottomSheet
import morz.example.archtemplate.core.designsystem.component.AppLoadingView
import morz.example.archtemplate.core.designsystem.component.TabsView
import morz.example.archtemplate.core.designsystem.component.rememberTabsViewState
import morz.example.archtemplate.core.designsystem.theme.AppTheme
import morz.example.archtemplate.core.model.ToDo
import morz.example.archtemplate.core.ui.DevicePreviews
import morz.example.archtemplate.core.resources.R
import morz.example.archtemplate.core.ui.todo.ToDoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onUpdateClick: (String) -> Unit
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var itemIdToAct by rememberSaveable { mutableIntStateOf(0) }
    val deleteEvent by viewModel.deleteEvent.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(deleteEvent) {
        when (deleteEvent) {
            is DeleteEvent.Success -> {
                showBottomSheet = false
            }

            is DeleteEvent.Failure -> {}
            else -> {}
        }
    }

    HomeScreen(
        modifier = modifier,
        homeUiState = homeUiState,
        onDelete = viewModel::deleteTodoItem,
        onItemClick = { toDoId ->
            showBottomSheet = true
            itemIdToAct = toDoId
        },
        onBottomSheetDismiss = { showBottomSheet = false },
        showBottomSheet = showBottomSheet,
        itemIdToAct = itemIdToAct,
        onUpdateClick = {
            showBottomSheet = false
            onUpdateClick(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    onDelete: (String) -> Unit,
    onItemClick: (Int) -> Unit,
    onBottomSheetDismiss: () -> Unit,
    showBottomSheet: Boolean,
    itemIdToAct: Int,
    onUpdateClick: (String) -> Unit
) {

    val tabsState = rememberTabsViewState(0)

    if (showBottomSheet) {
        ActionsBottomSheet(
            modifier = modifier,
            onBottomSheetDismiss = onBottomSheetDismiss,
            onDelete = onDelete,
            itemIdToAct = itemIdToAct,
            onUpdateClick = onUpdateClick
        )
    }

    when (homeUiState) {
        is HomeUiState.Loading -> {
            AppLoadingView(modifier = modifier)
        }

        is HomeUiState.Success -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TabsView(
                    modifier = Modifier.testTag("TabsView"),
                    state = tabsState,
                    titles = listOf("List", "Grid")
                )

                when (tabsState.selectedTabIndex) {
                    0 -> ToDoList(homeUiState.homeData.data, onItemClick, modifier)
                    1 -> ToDoGrid(homeUiState.homeData.data, onItemClick, modifier)
                }

            }
        }

        is HomeUiState.Error -> {
            val context = LocalContext.current
            Toast.makeText(context, homeUiState.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

}

@Composable
fun ToDoList(
    todos: List<ToDo>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(
            todos.size,
            { index -> todos[index].id ?: 0 }
        ) { index ->
            ToDoItem(
                todo = todos[index],
                onItemClick = onItemClick,
                modifier = modifier
            )
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ToDoGrid(
    todos: List<ToDo>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val columnWidth = if (screenWidth > 600.dp) {
        120.dp
    } else { // Mobile
        150.dp
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(150.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(todos.size) { index ->

                ToDoGridItem(
                    todo = todos[index],
                    onItemClick = onItemClick,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .wrapContentHeight(),
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ToDoGridItem(
    modifier: Modifier = Modifier,
    todo: ToDo,
    onItemClick: (Int) -> Unit,
) {
    Card(
        modifier = modifier
            .clickable { onItemClick(todo.id!!) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "To Do #${todo.id}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = todo.todo ?: "",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter =
                    painterResource(
                        if (todo.completed ?: false)
                            R.drawable.ic_completed
                        else
                            R.drawable.ic_not_completed,
                    ),
                contentDescription = "todo status image",
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionsBottomSheet(
    modifier: Modifier = Modifier,
    onBottomSheetDismiss: () -> Unit,
    onDelete: (String) -> Unit,
    itemIdToAct: Int,
    onUpdateClick: (String) -> Unit
) {

    AppBottomSheet(
        modifier = modifier,
        onDismiss = onBottomSheetDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Choose Action")
            Spacer(Modifier.height(16.dp))
            ListItem(
                modifier = Modifier
                    .clickable { onDelete(itemIdToAct.toString()) },
                headlineContent = { Text("Remove", color = Color.Red) },
                leadingContent = {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Localized description",
                        tint = Color.Red
                    )
                }
            )
            Spacer(Modifier.height(4.dp))
            ListItem(
                modifier = Modifier
                    .clickable {
                        onUpdateClick(itemIdToAct.toString())
                    },
                headlineContent = { Text("Update") },
                leadingContent = {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = "Localized description"
                    )
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            homeUiState = HomeUiState.Success(
                homeData = HomeData(
                    data = listOf(
                        ToDo(1, "adasdasd", true, 123),
                        ToDo(2, "adasdasd", false, 123),
                        ToDo(3, "adasdasd", false, 123),
                        ToDo(4, "adasdasd", true, 123),
                    )
                )
            ),
            onDelete = {},
            onItemClick = {},
            onBottomSheetDismiss = {},
            showBottomSheet = false,
            itemIdToAct = 0,
            onUpdateClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
private fun ToDoGridItemPreview() {
    val todo = ToDo(1, "adasdasd", true, 123)
    ToDoGridItem(
        todo = todo,
        onItemClick = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
private fun ToDoListItemPreview() {
    val todo = ToDo(1, "adasdasd", true, 123)
    ToDoItem(
        todo = todo,
        onItemClick = {},
    )
}

