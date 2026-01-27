package com.example.exammitrabykaushal.DataLayer.Database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.exammitrabykaushal.DataLayer.Dao.HistoryDao
import com.example.exammitrabykaushal.DataLayer.Entity.TestResult
import kotlinx.coroutines.flow.Flow


// Database (The Connection)
@Database(
    entities = [TestResult::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun testHistoryDao(): HistoryDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL(
                    "ALTER TABLE test_history ADD COLUMN correctCount INTEGER NOT NULL DEFAULT 0"
                )
                database.execSQL(
                    "ALTER TABLE test_history ADD COLUMN wrongCount INTEGER NOT NULL DEFAULT 0"
                )
                database.execSQL(
                    "ALTER TABLE test_history ADD COLUMN timeTakenSeconds INTEGER NOT NULL DEFAULT 0"
                )
                database.execSQL(
                    "ALTER TABLE test_history ADD COLUMN timestamp INTEGER NOT NULL DEFAULT 0"
                )
                database.execSQL(
                    "ALTER TABLE test_history ADD COLUMN totalMarks INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "test_history_db"
                )
                    .addMigrations(MIGRATION_2_3)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
