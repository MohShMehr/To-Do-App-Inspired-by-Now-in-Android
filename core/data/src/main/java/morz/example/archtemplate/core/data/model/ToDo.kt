package morz.example.archtemplate.core.data.model

import morz.example.archtemplate.core.model.ToDo
import morz.example.archtemplate.core.network.model.toDo.NetworkToDo
import morz.example.archtemplate.core.network.model.toDo.NetworkToDoResponse

fun NetworkToDo.asExternalModel() = ToDo(
    id = id,
    todo = todo,
    completed = completed,
    userId = userId

)

fun ToDo.asNetworkModel() = NetworkToDo(
    id = id,
    todo = todo,
    completed = completed,
    userId = userId
)

fun NetworkToDoResponse.mapToToDos() : List<ToDo> {
    return todos.map { item->
        ToDo(
            id = item.id,
            todo = item.todo,
            completed = item.completed,
            userId = item.userId
        )
    }
}