package morz.example.archtemplate.core.network.model.toDo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkToDo(
    var id: Int? = null,
    var todo: String? = null,
    var completed: Boolean? = null,
    var userId: Int? = null,
    var isDeleted: Boolean? = null,
    var deletedOn: String? = null,
)