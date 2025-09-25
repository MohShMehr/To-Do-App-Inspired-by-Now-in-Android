package com.android.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import morz.example.archtemplate.core.designsystem.component.SearchBar
import morz.example.archtemplate.core.designsystem.theme.AppTheme
import morz.example.archtemplate.core.designsystem.theme.ColorFF6B6B

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
                    -> Unit
                is SearchResultUiState.Success -> {
                    if (searchResultUiState.isEmpty()) {

                    }else{
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = searchResultUiState.todos.toString(),
                                color = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    AppTheme {
        SearchScreen()
    }
}