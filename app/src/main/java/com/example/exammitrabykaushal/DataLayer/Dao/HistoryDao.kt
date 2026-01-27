package com.example.exammitrabykaushal.DataLayer.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exammitrabykaushal.DataLayer.Entity.TestResult
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM test_history ORDER BY date DESC")
    fun getAllHistory(): Flow<List<TestResult>>

    @Query("""SELECT * FROM test_history WHERE testName = :testName ORDER BY score DESC LIMIT 1 """)
    fun getBestScore(testName: String) : Flow<TestResult?>

    @Query("""SELECT date FROM test_history ORDER BY date DESC""")
    suspend fun getAllTestDates(): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: TestResult)

    @Delete
    suspend fun deleteResult(result: TestResult)
}