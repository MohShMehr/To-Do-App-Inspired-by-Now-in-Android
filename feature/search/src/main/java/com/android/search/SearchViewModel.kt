package com.android.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import morz.example.archtemplate.core.domain.GetSearchContentsUseCase
import morz.example.archtemplate.core.domain.SearchContentsRepository
import morz.example.archtemplate.core.domain.ToDoRepository
import morz.example.archtemplate.core.model.SearchResult
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

private const val SEARCH_QUERY = "searchQuery"
private const val SEARCH_QUERY_MIN_LENGTH = 2
private const val SEARCH_MIN_FTS_ENTITY_COUNT = 1

@HiltViewModel
class SearchViewModel @Inject constructor(
    getSearchContentsUseCase: GetSearchContentsUseCase,
    private val savedStateHandle: SavedStateHandle,
    toDoRepository: ToDoRepository
) : ViewModel() {

    val searchResultUiState: StateFlow<SearchResultUiState> =
        (toDoRepository as SearchContentsRepository).getSearchContentsCount()
            .flatMapLatest { totalCount ->
                if (totalCount < SEARCH_MIN_FTS_ENTITY_COUNT) {
                    flowOf(SearchResultUiState.SearchNotReady)
                } else {
                    searchQuery.flatMapLatest { query ->
                        if (query.trim().length < SEARCH_QUERY_MIN_LENGTH) {
                            flowOf(SearchResultUiState.EmptyQuery)
                        } else {
                            getSearchContentsUseCase(query)
                                .map<SearchResult, SearchResultUiState> { data ->
                                    SearchResultUiState.Success(
                                        todos = data.todos
                                    )
                                }
                                .catch { emit(SearchResultUiState.LoadFailed) }
                        }
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = SearchResultUiState.Loading,
            )

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }
}