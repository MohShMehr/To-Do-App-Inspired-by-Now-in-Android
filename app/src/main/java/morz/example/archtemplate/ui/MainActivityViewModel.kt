package morz.example.archtemplate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import morz.example.archtemplate.core.domain.UserDataRepository
import morz.example.archtemplate.core.model.UserData
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository
) : ViewModel() {

    val uiState: StateFlow<UiState> = userDataRepository.userData.map {
        UiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = UiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}

sealed interface UiState {
    data object Loading : UiState
    data class Success(val userData: UserData) : UiState
}