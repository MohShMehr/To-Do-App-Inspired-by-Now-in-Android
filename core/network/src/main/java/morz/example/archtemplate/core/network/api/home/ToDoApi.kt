package morz.example.archtemplate.core.network.api.home

import morz.example.archtemplate.core.network.model.toDo.NetworkToDo
import morz.example.archtemplate.core.network.model.toDo.NetworkToDoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ToDoApi {
    @GET("todos/user/13")
    suspend fun getToDos(): Response<NetworkToDoResponse>

    @GET("todos/{id}")
    suspend fun getToDo(
        @Path("id") id: String
    ): Response<NetworkToDo>

    @POST("todos/add")
    suspend fun addToDo(
        @Body todo: NetworkToDo
    ): Response<NetworkToDo>

    @PUT("todos/{id}")
    suspend fun updateToDo(
        @Path("id") id: String,
        @Body todo: NetworkToDo
    ): Response<NetworkToDo>

    @DELETE("todos/{id}")
    suspend fun deleteToDo(
        @Path("id") id: String
    ): Response<NetworkToDo>
}