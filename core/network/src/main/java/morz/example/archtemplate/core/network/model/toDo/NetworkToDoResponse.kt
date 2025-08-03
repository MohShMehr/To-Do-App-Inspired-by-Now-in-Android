package morz.example.archtemplate.core.network.model.toDo

data class NetworkToDoResponse(
    var todos: List<NetworkToDo>,
    var total: Int,
    var skip: Int,
    var limit: Int
)