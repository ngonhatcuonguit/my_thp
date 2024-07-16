package com.cuongngo.core_project.data.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cuongngo.core_project.data.database.roomdb.DaoInterFace.FormDao
import com.cuongngo.core_project.data.database.roomdb.DaoInterFace.FormSchemaDao
import com.cuongngo.core_project.data.database.roomdb.DaoInterFace.GenreDao
import com.cuongngo.core_project.data.database.roomdb.DaoInterFace.RecordProcessDao
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.FormSchemaEntity
import com.cuongngo.core_project.data.database.roomdb.entity.GenreEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RecordProcessEntity

@Database(
    entities = [GenreEntity::class, RecordProcessEntity::class, FormEntity::class, FormSchemaEntity::class],
    version = 1,
    exportSchema = true
)


abstract class AppDatabase : RoomDatabase() {

    abstract fun genreDao(): GenreDao
    abstract fun recordProcessDao(): RecordProcessDao
    abstract fun thpFormDao(): FormDao
    abstract fun formSchemaDao(): FormSchemaDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE?:buildDatabase(context).also {
                INSTANCE = it
            }
        }
        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "CoreProjectDatabase.db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}