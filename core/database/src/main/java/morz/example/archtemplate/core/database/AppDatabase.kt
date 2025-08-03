package morz.example.archtemplate.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import morz.example.archtemplate.core.database.dao.WorkDao
import morz.example.archtemplate.core.database.model.WorkEntity

@Database(
    entities = [WorkEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workDao(): WorkDao
}