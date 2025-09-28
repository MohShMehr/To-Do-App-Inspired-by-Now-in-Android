package com.android.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import morz.example.archtemplate.core.designsystem.component.SearchBar
import morz.example.archtemplate.core.designsystem.theme.AppTheme
import morz.example.archtemplate.core.designsystem.theme.ColorFF6B6B
import morz.example.archtemplate.core.model.ToDo
import morz.example.archtemplate.core.ui.todo.ToDoItem

@Composable
internal fun SearchRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResultUiState by viewModel.searchResultUiState.collectAsStateWithLifecycle()

    SearchScreen(
        searchQuery = searchQuery,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        searchResultUiState = searchResultUiState
    )
}

@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit = {},
    searchResultUiState: SearchResultUiState = SearchResultUiState.Loading,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChanged,
                onSearch = {
                },
                modifier = Modifier.padding(bottom = 16.dp),
                iconBackground = ColorFF6B6B
            )
            when (searchResultUiState) {
                SearchResultUiState.Loading,
                SearchResultUiState.LoadFailed,
                    -> Unit
                SearchResultUiState.SearchNotReady -> Unit
                SearchResultUiState.EmptyQuery,
                    -> EmptySearchResultBody(
                    modifier = modifier,
                    searchQuery = searchQuery)
                is SearchResultUiState.Success -> {
                    if (searchResultUiState.isEmpty()) {
                        EmptySearchResultBody(
                            modifier = modifier,
                            searchQuery = searchQuery)
                    }else{
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            SearchResultBody(
                                items = searchResultUiState.todos,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultBody(
    modifier: Modifier = Modifier,
    items: List<ToDo>,
    onItemClick: (Int) -> Unit = {},
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(
            items.size,
            { index -> items[index].id ?: 0}
        ) { index ->
            ToDoItem(
                todo = items[index],
                onItemClick = onItemClick,
            )
        }
    }
}

@Composable
fun EmptySearchResultBody(
    modifier: Modifier = Modifier,
    searchQuery: String,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buildAnnotatedString {
                // "No" in red + bold
                withStyle(
                    style = SpanStyle(
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("No")
                }

                append(" results found for ")

                // search query in red
                withStyle(
                    style = SpanStyle(
                        color = Color.Red
                    )
                ) {
                    append(searchQuery)
                }
            },
            textAlign = TextAlign.Center
        )
    }
}



@Preview
@Composable
private fun SearchScreenPreview() {
    AppTheme {
        SearchScreen()
    }
}