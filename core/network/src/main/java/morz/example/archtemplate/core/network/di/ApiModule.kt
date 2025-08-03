package morz.example.archtemplate.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import morz.example.archtemplate.core.network.api.home.ToDoApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providesToDoApi(retrofit: Retrofit): ToDoApi {
        return retrofit.create(ToDoApi::class.java)
    }
}