package morz.example.archtemplate.core.domain

import kotlinx.coroutines.flow.Flow
import morz.example.archtemplate.core.model.ToDo
import morz.example.archtemplate.core.network.model.toDo.NetworkToDo
import morz.example.archtemplate.core.network.model.toDo.NetworkToDoResponse
import morz.example.archtemplate.core.network.util.ApiResult

interface ToDoRepository {

    fun getToDos(): Flow<ApiResult<NetworkToDoResponse>>

    fun getToDo(id: String): Flow<ApiResult<NetworkToDo>>

    suspend fun addToDo(todo: ToDo): ApiResult<NetworkToDo>

    suspend fun updateToDo(id: String, todo: ToDo): ApiResult<NetworkToDo>

    suspend fun deleteToDo(id: String): ApiResult<NetworkToDo>
}