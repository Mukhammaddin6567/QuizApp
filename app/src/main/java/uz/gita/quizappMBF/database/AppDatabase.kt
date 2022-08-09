package uz.gita.quizappMBF.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import timber.log.Timber

@Database(entities = [Entity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): Dao

    companion object {
        private lateinit var instance: AppDatabase

        fun init(application: Application) {
            instance =
                Room.databaseBuilder(application, AppDatabase::class.java, application.packageName)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
        }

        fun getDatabase(): AppDatabase {
            Timber.d("Room Database Instance: $instance")
            return instance
        }
    }
}