package morz.example.archtemplate.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import morz.example.archtemplate.UserSettings
import morz.example.archtemplate.core.common.network.di.Dispatcher
import morz.example.archtemplate.core.common.network.di.AppDispatchers
import morz.example.archtemplate.core.common.network.di.ApplicationScope
import morz.example.archtemplate.core.datastore.UserSettingsSerializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(AppDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        userSettingsSerializer: UserSettingsSerializer,
    ): DataStore<UserSettings> =
        DataStoreFactory.create(
            serializer = userSettingsSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile("user_settings.pb")
        }
}
