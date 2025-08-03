package morz.example.archtemplate.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import morz.example.archtemplate.UserSettings
import morz.example.archtemplate.core.model.UserData
import javax.inject.Inject

class AppPreferencesDataSource @Inject constructor(
    private val userPreference: DataStore<UserSettings>
) {
    val userData = userPreference.data
        .map {
            UserData(
                useDarkMode = it.darkMode
            )
        }

    suspend fun setUseDarkModePreference(useDarkMode: Boolean) {
        userPreference.updateData { currentSetting->
           currentSetting.toBuilder()
               .setDarkMode(useDarkMode)
               .build()
        }
    }
}