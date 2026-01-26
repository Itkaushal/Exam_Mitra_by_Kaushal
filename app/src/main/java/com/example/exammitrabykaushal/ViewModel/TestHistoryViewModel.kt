package com.example.exammitrabykaushal.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.exammitrabykaushal.DataLayer.AppDatabase
import com.example.exammitrabykaushal.DataLayer.HistoryDao
import com.example.exammitrabykaushal.DataLayer.TestResult
import com.example.exammitrabykaushal.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TestHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: HistoryDao = AppDatabase.getInstance(application).testHistoryDao()
    private val repository = HistoryRepository(dao)

    // exposed as state flow for compose
    val historyList = repository.allHistory.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun insertResult(result: TestResult){
        viewModelScope.launch {
            repository.insertResult(result)
        }
    }

    fun deleteHistory(result: TestResult) {
        viewModelScope.launch {
            repository.deleteResult(result)
        }
    }

    fun getBestScore(testName: String) : Flow<TestResult?> {
        return repository.getBestScore(testName)
    }
}