package morz.example.archtemplate.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import morz.example.archtemplate.core.database.model.WorkEntity

@Dao
interface WorkDao {
    @Query(
        value = """
        SELECT * FROM works
        WHERE id = :workId
    """,
    )
    fun getWorkEntity(workId: String): Flow<WorkEntity>

    @Upsert
    suspend fun upsertWorks(entities: List<WorkEntity>)
}