package morz.example.archtemplate.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import morz.example.archtemplate.core.domain.UserDataRepository
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel(){
    val settingsUiState: StateFlow<SettingsUiState> =
        userDataRepository.userData
            .map { userData ->
                SettingsUiState.Success(
                    settings = UserSettings(useDarkMode = userData.useDarkMode),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsUiState.Loading,
            )



    fun updateUseDarkModePreference(useDarkMode: Boolean) {
        viewModelScope.launch {
            userDataRepository.setUseDarMode(useDarkMode)
        }
    }

}

data class UserSettings(
    val useDarkMode: Boolean
)

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Success(val settings: UserSettings) : SettingsUiState
}