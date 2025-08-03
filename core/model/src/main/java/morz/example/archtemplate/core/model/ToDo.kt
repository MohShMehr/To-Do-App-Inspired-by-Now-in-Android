package morz.example.archtemplate.core.model

data class ToDo(
    var id: Int? = null,
    var todo: String? = null,
    var completed: Boolean? = null,
    var userId: Int? = null,
    var isDeleted: Boolean? = null,
    var deletedOn: String? = null,
)
