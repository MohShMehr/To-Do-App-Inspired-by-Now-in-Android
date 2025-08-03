package morz.example.archtemplate.feature.tododetail

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import morz.example.archtemplate.core.designsystem.component.AppLoadingView
import morz.example.archtemplate.core.designsystem.theme.AppTheme

@Composable
internal fun ToDoDetailRoute(
    toDoId: String,
    modifier: Modifier = Modifier,
    viewModel: ToDoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTodo(toDoId)
    }

    when (uiState) {
        is UiState.Loading -> {
            AppLoadingView(modifier = modifier)
        }

        is UiState.Success -> {
            var titleText by rememberSaveable {
                mutableStateOf(
                    (uiState as UiState.Success).data.todo ?: ""
                )
            }
            var completed by rememberSaveable {
                mutableStateOf(
                    (uiState as UiState.Success).data.completed ?: false
                )
            }
            val isCreate = toDoId.toInt() == 0
            val bTitle = if (isCreate) "Create new ToDo" else "Update ToDo"
            ToDoDetailScreen(
                modifier = modifier,
                onCreateClick = {
                    (uiState as UiState.Success).data.apply {
                        todo = titleText
                        this.completed = completed
                        if (isCreate)
                            viewModel.add(this)
                        else
                            viewModel.update(this)
                    }
                },
                onValueChange = { value -> titleText = value },
                titleText = titleText,
                onStatusChange = { isCompleted -> completed = isCompleted },
                completed = completed,
                buttonTitle = bTitle
            )
        }

        is UiState.Error -> {
            val error = uiState as UiState.Error
            val context = LocalContext.current
            Toast.makeText(context, error.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
internal fun ToDoDetailScreen(
    modifier: Modifier = Modifier,
    onCreateClick: () -> Unit,
    onValueChange: (String) -> Unit,
    titleText: String,
    onStatusChange: (Boolean) -> Unit,
    completed: Boolean,
    buttonTitle: String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "New ToDo",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Title",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = titleText,
                onValueChange = { onValueChange(it) })

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Status",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = !completed, onClick = { onStatusChange(false) })
                Text(
                    text = "ToDo",
                    fontSize = 14.sp
                )
                RadioButton(selected = completed, onClick = { onStatusChange(true) })
                Text(
                    text = "Done",
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                onClick = { onCreateClick() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(buttonTitle, color = Color.White)
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        ToDoDetailScreen(
            onCreateClick = {},
            onValueChange = {},
            titleText = "abcde",
            onStatusChange = {},
            completed = true,
            buttonTitle = "Update"
        )
    }
}