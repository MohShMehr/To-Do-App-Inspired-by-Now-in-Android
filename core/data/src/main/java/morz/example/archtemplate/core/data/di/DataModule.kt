package morz.example.archtemplate.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import morz.example.archtemplate.core.data.repository.DefaultToDoRepository
import morz.example.archtemplate.core.data.repository.DefaultUserDataRepository
import morz.example.archtemplate.core.domain.SearchContentsRepository
import morz.example.archtemplate.core.domain.ToDoRepository
import morz.example.archtemplate.core.domain.UserDataRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsUserDataRepository(
        defaultUserDataRepository: DefaultUserDataRepository
    ): UserDataRepository

    @Binds
    fun bindsToDoRepository(
        defaultToDoRepository: DefaultToDoRepository,
    ): ToDoRepository

    @Binds
    fun bindSearchContentsRepository(
        defaultToDoRepository: DefaultToDoRepository
    ): SearchContentsRepository
}