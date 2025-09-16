package morz.example.archtemplate.core.domain

import kotlinx.coroutines.flow.Flow
import morz.example.archtemplate.core.model.SearchResult

interface SearchContentsRepository {

    fun searchContents(searchQuery: String): Flow<SearchResult>

    fun getSearchContentsCount(): Flow<Int>
}
