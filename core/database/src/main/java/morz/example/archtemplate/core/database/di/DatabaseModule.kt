package morz.example.archtemplate.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import morz.example.archtemplate.core.database.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesKarotikDatabase(
        @ApplicationContext context: Context
    ) : AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database"
    ).build()
}