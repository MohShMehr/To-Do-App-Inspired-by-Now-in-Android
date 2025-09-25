package morz.example.archtemplate.core.domain

import kotlinx.coroutines.flow.Flow
import morz.example.archtemplate.core.model.SearchResult
import javax.inject.Inject

class GetSearchContentsUseCase @Inject constructor(
    private val searchContentsRepository: SearchContentsRepository,
) {
    operator fun invoke(
        searchQuery: String,
    ): Flow<SearchResult> =
        searchContentsRepository.searchContents(searchQuery)
}
