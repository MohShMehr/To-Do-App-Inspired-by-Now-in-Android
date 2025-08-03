package morz.example.archtemplate.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import morz.example.archtemplate.core.data.model.asNetworkModel
import morz.example.archtemplate.core.domain.ToDoRepository
import morz.example.archtemplate.core.model.ToDo
import morz.example.archtemplate.core.network.api.home.ToDoApi
import morz.example.archtemplate.core.network.model.toDo.NetworkToDo
import morz.example.archtemplate.core.network.model.toDo.NetworkToDoResponse
import morz.example.archtemplate.core.network.util.APiHelper.safeApiCall
import morz.example.archtemplate.core.network.util.ApiResult
import javax.inject.Inject

class DefaultToDoRepository @Inject constructor(
    private val toDoApi: ToDoApi
) : ToDoRepository {
    override fun getToDos(): Flow<ApiResult<NetworkToDoResponse>> = flow {
        emit(safeApiCall { toDoApi.getToDos() })
    }

    override fun getToDo(id: String): Flow<ApiResult<NetworkToDo>> = flow {
        emit(safeApiCall { toDoApi.getToDo(id) })
    }

    override suspend fun addToDo(todo: ToDo): ApiResult<NetworkToDo> =
        safeApiCall { toDoApi.addToDo(todo.asNetworkModel()) }

    override suspend fun updateToDo(id: String, todo: ToDo): ApiResult<NetworkToDo> =
        safeApiCall { toDoApi.updateToDo(id = id, todo = todo.asNetworkModel()) }

    override suspend fun deleteToDo(id: String): ApiResult<NetworkToDo> =
        safeApiCall { toDoApi.deleteToDo(id) }
}