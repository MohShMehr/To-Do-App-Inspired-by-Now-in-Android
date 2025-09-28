package morz.example.archtemplate.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import morz.example.archtemplate.core.data.model.asExternalModel
import morz.example.archtemplate.core.data.model.asNetworkModel
import morz.example.archtemplate.core.domain.SearchContentsRepository
import morz.example.archtemplate.core.domain.ToDoRepository
import morz.example.archtemplate.core.model.SearchResult
import morz.example.archtemplate.core.model.ToDo
import morz.example.archtemplate.core.network.api.home.ToDoApi
import morz.example.archtemplate.core.network.model.toDo.NetworkToDo
import morz.example.archtemplate.core.network.model.toDo.NetworkToDoResponse
import morz.example.archtemplate.core.network.util.APiHelper.safeApiCall
import morz.example.archtemplate.core.network.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.minutes

@Singleton
class DefaultToDoRepository @Inject constructor(
    private val toDoApi: ToDoApi
) : ToDoRepository, SearchContentsRepository {

    private var todoCache: ApiResult<NetworkToDoResponse>? = null
    private var lastCacheTime: Long = 0
    private var cacheState: CacheState = CacheState.Empty

    private val cacheStateTimeout = 5.minutes.inWholeMilliseconds

    enum class CacheState {
        Empty, Loading, Success, Error
    }

    private fun invalidateCache() {
        todoCache = null
        lastCacheTime = 0
        cacheState = CacheState.Empty
    }

    private fun isCacheValid(): Boolean {
        return cacheState == CacheState.Success &&
                (System.currentTimeMillis() - lastCacheTime) < cacheStateTimeout

    }

    private fun updateCache(result: ApiResult<NetworkToDoResponse>) {
        todoCache = result
        lastCacheTime = System.currentTimeMillis()
        cacheState = when (result) {
            is ApiResult.Success -> CacheState.Success
            is ApiResult.Error -> CacheState.Error
        }
    }

    override fun getToDos(): Flow<ApiResult<NetworkToDoResponse>> = flow {
        if (isCacheValid()) {
            todoCache?.let { emit(it) }
            return@flow
        }

        cacheState = CacheState.Loading

        val resp = safeApiCall { toDoApi.getToDos() }
        updateCache(resp)
        emit(resp)
    }

    override fun getToDo(id: String): Flow<ApiResult<NetworkToDo>> = flow {

        if (isCacheValid()) {
            todoCache?.let { cache ->
                if (cache is ApiResult.Success) {
                    val cachedTodo = cache.data.todos.find { it.id.toString() == id }
                    if (cachedTodo != null) {
                        val hasCompleteData = cachedTodo.todo != null && 
                                            cachedTodo.completed != null && 
                                            cachedTodo.userId != null
                        
                        if (hasCompleteData) {
                            emit(ApiResult.Success(cachedTodo))
                            return@flow
                        }
                    }
                }
            }
        }

        val result = safeApiCall { toDoApi.getToDo(id) }

        if (result is ApiResult.Success && isCacheValid()) {
            todoCache?.let { cache ->
                if (cache is ApiResult.Success) {
                    val updatedTodos = cache.data.todos.map { todo ->
                        if (todo.id.toString() == id) {
                            result.data
                        } else {
                            todo
                        }
                    }
                    val updatedCache = cache.copy(data = cache.data.copy(todos = updatedTodos))
                    todoCache = updatedCache
                }
            }
        }
        
        emit(result)
    }

    override suspend fun addToDo(todo: ToDo): ApiResult<NetworkToDo> {
        val result = safeApiCall { toDoApi.addToDo(todo.asNetworkModel()) }
        if (result is ApiResult.Success) {
            invalidateCache()
        }
        return result
    }

    override suspend fun updateToDo(id: String, todo: ToDo): ApiResult<NetworkToDo> {
        val result = safeApiCall { toDoApi.updateToDo(id = id, todo = todo.asNetworkModel()) }
        if (result is ApiResult.Success) {
            invalidateCache()
        }
        return result
    }

    override suspend fun deleteToDo(id: String): ApiResult<NetworkToDo> {
        val result = safeApiCall { toDoApi.deleteToDo(id) }
        if (result is ApiResult.Success) {
            invalidateCache()
        }
        return result
    }

    override fun searchContents(searchQuery: String): Flow<SearchResult> = flow {
        if (isCacheValid())
            todoCache?.let { cache ->
                if (cache is ApiResult.Success) {
                    val filteredTodos = if (searchQuery.isBlank()) {
                        cache.data.todos
                    } else {
                        cache.data.todos.filter { todo ->
                            todo.todo?.contains(searchQuery, ignoreCase = true) == true
                        }
                    }
                    emit(SearchResult(todos = filteredTodos.map { it.asExternalModel() }))
                } else {
                    emit(SearchResult(todos = emptyList()))
                }
            } ?: run {
                emit(SearchResult(todos = emptyList()))
            }
        else
            emit(SearchResult(todos = emptyList()))
    }

    override fun getSearchContentsCount(): Flow<Int> = flow {
        if (isCacheValid())
            todoCache?.let { cache ->
                if (cache is ApiResult.Success) {
                    emit(cache.data.todos.size)
                } else {
                    emit(0)
                }
            } ?: run {
                emit(0)
            }
        else
            emit(0)
    }
}