package morz.example.archtemplate

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TestModule {
    
    // You can add test implementations here if needed
    // For example, if you want to provide a mock UserDataRepository:
    /*
    @Provides
    @Singleton
    fun provideTestUserDataRepository(): UserDataRepository {
        return object : UserDataRepository {
            override val userData: Flow<UserData> = flow {
                emit(UserData(useDarkMode = false))
            }
        }
    }
    */
} 