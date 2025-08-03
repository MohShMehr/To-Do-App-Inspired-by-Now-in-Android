package morz.example.archtemplate.core.data.repository

import kotlinx.coroutines.flow.Flow
import morz.example.archtemplate.core.datastore.AppPreferencesDataSource
import morz.example.archtemplate.core.domain.UserDataRepository
import morz.example.archtemplate.core.model.UserData
import javax.inject.Inject

class DefaultUserDataRepository @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource,
) : UserDataRepository {
    override val userData: Flow<UserData> = appPreferencesDataSource.userData

    override suspend fun setUseDarMode(useDarkMode: Boolean) {
        appPreferencesDataSource.setUseDarkModePreference(useDarkMode)
    }
}