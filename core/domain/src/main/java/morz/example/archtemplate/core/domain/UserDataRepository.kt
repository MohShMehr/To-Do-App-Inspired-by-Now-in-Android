package morz.example.archtemplate.core.domain

import kotlinx.coroutines.flow.Flow
import morz.example.archtemplate.core.model.UserData

interface UserDataRepository {
    val userData: Flow<UserData>
    suspend fun setUseDarMode(useDarkMode: Boolean)
}