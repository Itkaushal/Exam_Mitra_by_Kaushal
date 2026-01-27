package com.example.exammitrabykaushal.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.exammitrabykaushal.DataLayer.Dao.HistoryDao
import com.example.exammitrabykaushal.DataLayer.Database.AppDatabase
import com.example.exammitrabykaushal.DataLayer.Entity.TestResult
import com.example.exammitrabykaushal.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileStats(
    val accuracy: Int = 0,
    val streakDays: Int = 0,
    val rank: Int = 0
)
class TestHistoryViewModel(application: Application,
    ) : AndroidViewModel(application) {

    private val dao: HistoryDao = AppDatabase.getInstance(application).testHistoryDao()
    private val repository = HistoryRepository(dao)


    // exposed as state flow for compose
    val historyList = repository.allHistory.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    // Profile States

    val profileStats: StateFlow<ProfileStats> =
        historyList.map { list ->
            if (list.isEmpty()) return@map ProfileStats()
            val totalCorrect = list.sumOf { it.correctCount }
            val totalQuestions = list.sumOf { it.totalQuestions }

            val accuracy =
                if (totalQuestions > 0)
                    ((totalCorrect * 100f) / totalQuestions).toInt()
                else 0
            ProfileStats(
                accuracy = accuracy,
                streakDays = calculateStreak(list),
                rank = calculateRank(accuracy)
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ProfileStats()
        )

    // fun calculate streak
    private fun calculateStreak(list: List<TestResult>): Int {
        val sorted = list.sortedByDescending { it.date }
        var streak = 0
        var lastDay: Long? = null

        for (item in sorted) {
            val day = item.date / (24 * 60 * 60 * 1000)
            if (lastDay == null || lastDay == day + 1) {
                streak++
                lastDay = day
            } else break
        }
        return streak
    }

    // fun calculate rank
    private fun calculateRank(accuracy: Int): Int =
        when {
            accuracy >= 90 -> 250
            accuracy >= 75 -> 850
            accuracy >= 60 -> 2000
            else -> 5000
        }


    // fun insert result
    fun insertResult(result: TestResult){
        viewModelScope.launch {
            repository.insertResult(result)
        }
    }

    // fun delete history
    fun deleteHistory(result: TestResult) {
        viewModelScope.launch {
            repository.deleteResult(result)
        }
    }

    // fun get best score
    fun getBestScore(testName: String) : Flow<TestResult?> {
        return repository.getBestScore(testName)
    }
}