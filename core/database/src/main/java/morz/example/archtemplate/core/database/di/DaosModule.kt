package morz.example.archtemplate.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import morz.example.archtemplate.core.database.AppDatabase
import morz.example.archtemplate.core.database.dao.WorkDao

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesWorkDao(
        database: AppDatabase,
    ): WorkDao = database.workDao()
}