package com.example.exammitrabykaushal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exammitrabykaushal.DataLayer.Dao.HistoryDao
import com.example.exammitrabykaushal.UIScreens.component.calculateStreak
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val dao: HistoryDao
) : ViewModel() {

    private val _streak = MutableStateFlow(0)
    val streak = _streak.asStateFlow()

    init {
        loadStreak()
    }

    private fun loadStreak() {
        viewModelScope.launch {
            val dates = dao.getAllTestDates()
            _streak.value = calculateStreak(dates)
        }
    }
}
