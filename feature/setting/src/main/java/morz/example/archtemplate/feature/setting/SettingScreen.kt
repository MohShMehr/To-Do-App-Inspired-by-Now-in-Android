package morz.example.archtemplate.feature.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import morz.example.archtemplate.core.designsystem.theme.AppTheme
import morz.example.archtemplate.core.ui.DevicePreviews

@Composable
internal fun SettingRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    SettingRoute(
        modifier = modifier,
        settingsUiState = settingsUiState,
        onChangeDarkTheme = viewModel::updateUseDarkModePreference
        )
}

@Composable
internal fun SettingRoute(
    modifier: Modifier = Modifier,
    settingsUiState: SettingsUiState,
    onChangeDarkTheme: (useDarkThem: Boolean) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when (settingsUiState) {
            is SettingsUiState.Loading -> {
                Text("Setting")
            }
            is SettingsUiState.Success -> {
                SettingsTitle(text = stringResource(R.string.theme))
                Column(Modifier.selectableGroup()) {
                    SettingsSelectorRow(
                        text = stringResource(R.string.mode_dark),
                        selected = settingsUiState.settings.useDarkMode,
                        onClick = { onChangeDarkTheme(true) },
                    )
                    SettingsSelectorRow(
                        text = stringResource(R.string.mode_light),
                        selected = !settingsUiState.settings.useDarkMode,
                        onClick = { onChangeDarkTheme(false) },
                    )
                }
            }
        }
    }

}

@Composable
private fun SettingsTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}


@Composable
fun SettingsSelectorRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@DevicePreviews
@Composable
private fun PreviewSettingsDialogLoading() {
    AppTheme {
        SettingRoute(
            settingsUiState = SettingsUiState.Success(UserSettings(false)),
            onChangeDarkTheme = {},
        )
    }
}
