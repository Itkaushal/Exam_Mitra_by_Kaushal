package com.example.exammitrabykaushal.DataLayer

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// 1. Entity (The Table)
@Entity(tableName = "test_history")
data class TestResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val testName: String,
    val score: Int,
    val totalQuestions: Int,
    val date: Long = System.currentTimeMillis(),
    val correctCount: Int,
    val wrongCount: Int,
    val timeTakenSeconds: Int
)

// 2. DAO (The Access Methods)
@Dao
interface HistoryDao {
    @Query("SELECT * FROM test_history ORDER BY date DESC")
    fun getAllHistory(): Flow<List<TestResult>>

    @Query("""SELECT * FROM test_history WHERE testName = :testName ORDER BY score DESC LIMIT 1 """)
    fun getBestScore(testName: String) : Flow<TestResult?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: TestResult)
}

// 3. Database (The Connection)
@Database(entities = [TestResult::class], version = 2, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {

    abstract fun testHistoryDao(): HistoryDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "test_history_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}