package com.cuongngo.core_project.data.database
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cuongngo.core_project.data.database.roomdb.DaoInterFace.FormDao
import com.cuongngo.core_project.data.database.roomdb.DaoInterFace.GdDao
import com.cuongngo.core_project.data.database.roomdb.DaoInterFace.RequestDao
import com.cuongngo.core_project.data.database.roomdb.DaoInterFace.GenreDao
import com.cuongngo.core_project.data.database.roomdb.DaoInterFace.UserDao
import com.cuongngo.core_project.data.database.roomdb.entity.Converters
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.GdEntity
import com.cuongngo.core_project.data.database.roomdb.entity.GenreEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity

@Database(
    entities = [GenreEntity::class, FormEntity::class, RequestEntity::class, UserTHPEntity::class, GdEntity::class],
    version = 1,
    exportSchema = true
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun thpFormDao(): FormDao
    abstract fun requestDao(): RequestDao
    abstract fun userDao(): UserDao
    abstract fun gdDao(): GdDao

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