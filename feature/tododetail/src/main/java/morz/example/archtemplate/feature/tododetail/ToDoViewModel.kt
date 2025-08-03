package morz.example.archtemplate.feature.tododetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import morz.example.archtemplate.core.data.model.asExternalModel
import morz.example.archtemplate.core.domain.ToDoRepository
import morz.example.archtemplate.core.model.ToDo
import morz.example.archtemplate.core.network.util.ApiResult
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    fun loadTodo(id: String) {
        viewModelScope.launch {
            if (id.toInt() != 0) {
                toDoRepository.getToDo(id)
                    .map {
                        when (it) {
                            is ApiResult.Success -> {
                                UiState.Success(
                                    data = it.data.asExternalModel()
                                )
                            }

                            is ApiResult.Error -> {
                                UiState.Error(
                                    errorMessage = it.throwable.message ?: "Error happened"
                                )
                            }
                        }
                    }.collect { uiState ->
                        _uiState.value = uiState
                    }
            } else {
                _uiState.value = UiState.Success(
                    data = ToDo(id = 0, todo =  "", completed =  false, userId =  13)
                )
            }
        }
    }

    fun update(todo: ToDo) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val result = toDoRepository.updateToDo(
                    id = todo.id.toString(),
                    todo = ToDo(todo = todo.todo, completed = todo.completed)
                )
                when (result) {
                    is ApiResult.Success -> {
                        _uiState.value = UiState.Success(data = result.data.asExternalModel())
                    }

                    is ApiResult.Error -> {
                        UiState.Error(
                            errorMessage = result.throwable.message ?: "Error happened"
                        )
                    }
                }


            } catch (e: Exception) {
            }
        }
    }

    fun add(todo: ToDo) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val result = toDoRepository.addToDo(todo = todo)
                when (result) {
                    is ApiResult.Success -> {
                        _uiState.value = UiState.Success(data = result.data.asExternalModel())
                    }

                    is ApiResult.Error -> {
                        UiState.Error(
                            errorMessage = result.throwable.message ?: "Error happened"
                        )
                    }
                }

            } catch (e: Exception) {
            }
        }
    }
}

sealed interface UiState {
    data object Loading : UiState
    data class Success(val data: ToDo) : UiState
    data class Error(val errorMessage: String) : UiState
}