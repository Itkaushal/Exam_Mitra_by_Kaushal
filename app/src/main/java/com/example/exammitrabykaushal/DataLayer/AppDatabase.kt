package com.example.exammitrabykaushal.DataLayer

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// 1. Entity (The Table)
@Entity(tableName = "test_history")
data class TestResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val testName: String,     // e.g., "Math Mock", "SSC CGL PYQ"
    val score: Int,           // e.g., 15
    val totalQuestions: Int,  // e.g., 20
    val date: Long = System.currentTimeMillis() // Timestamp
)

// 2. DAO (The Access Methods)
@Dao
interface HistoryDao {
    @Query("SELECT * FROM test_history ORDER BY date DESC")
    fun getAllHistory(): Flow<List<TestResult>> // Returns a live stream of data

    @Insert
    suspend fun insertResult(result: TestResult)
}

// 3. Database (The Connection)
@Database(entities = [TestResult::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "exam_mitra_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}