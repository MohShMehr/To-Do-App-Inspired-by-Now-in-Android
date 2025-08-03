package morz.example.archtemplate.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import morz.example.archtemplate.core.data.model.asExternalModel
import morz.example.archtemplate.core.data.model.mapToToDos
import morz.example.archtemplate.core.domain.ToDoRepository
import morz.example.archtemplate.core.model.ToDo
import morz.example.archtemplate.core.network.util.ApiResult
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        viewModelScope.launch {
            toDoRepository.getToDos()
                .map {
                    when (it) {
                        is ApiResult.Success -> {
                            HomeUiState.Success(
                                homeData = HomeData(data = (it.data).mapToToDos())
                            )
                        }

                        is ApiResult.Error -> {
                            HomeUiState.Error(it.throwable.message ?: "Error happened")
                        }
                    }
                }.collect { uiState ->
                    _homeUiState.value = uiState
                }
        }
    }

    private val _deleteEvent = MutableSharedFlow<DeleteEvent>()
    val deleteEvent: SharedFlow<DeleteEvent> = _deleteEvent

    fun deleteTodoItem(id: String) {
        viewModelScope.launch {
            try {
                val apiResult = toDoRepository.deleteToDo(id)

                when (apiResult) {
                    is ApiResult.Success -> {
                        _deleteEvent.emit(DeleteEvent.Success(apiResult.data.asExternalModel()))
                    }

                    is ApiResult.Error -> {
                        _deleteEvent.emit(DeleteEvent.Failure(apiResult.throwable.message ?: "Error happened"))
                    }
                }


                if (_homeUiState.value is HomeUiState.Success)
                    _homeUiState.update { currentState ->
                        if (currentState is HomeUiState.Success) {
                            currentState.copy(
                                homeData = currentState.homeData.copy(
                                    data = currentState.homeData.data.filterNot { it.id.toString() == id }
                                )
                            )
                        } else {
                            currentState
                        }
                    }
            } catch (e: Exception) {
                _deleteEvent.emit(DeleteEvent.Failure(e.message ?: "Unknown error"))
            }
        }
    }
}


sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val homeData: HomeData) : HomeUiState
    data class Error(val errorMessage: String) : HomeUiState
}

sealed class DeleteEvent {
    data class Success(val deletedTodo: ToDo) : DeleteEvent()
    data class Failure(val message: String) : DeleteEvent()
}

data class HomeData(var data: List<ToDo>)