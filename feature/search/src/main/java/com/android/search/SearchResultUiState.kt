package com.android.search

import morz.example.archtemplate.core.model.ToDo

sealed interface SearchResultUiState {
    data object Loading : SearchResultUiState

    data object EmptyQuery : SearchResultUiState

    data object LoadFailed : SearchResultUiState

    data class Success(
        val todos: List<ToDo> = emptyList()
    ) : SearchResultUiState {
        fun isEmpty(): Boolean = todos.isEmpty()
    }
    data object SearchNotReady : SearchResultUiState
}