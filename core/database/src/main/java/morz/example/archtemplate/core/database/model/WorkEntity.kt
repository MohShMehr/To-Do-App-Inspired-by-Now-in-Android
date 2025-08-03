package morz.example.archtemplate.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "works"
)
data class WorkEntity(
    @PrimaryKey
    val id: String,
    val title: String,
)