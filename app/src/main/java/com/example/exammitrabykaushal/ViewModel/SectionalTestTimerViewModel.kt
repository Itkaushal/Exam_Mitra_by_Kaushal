package com.example.exammitrabykaushal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SectionalTestTimerViewModel : ViewModel() {

    private val _remainingTime = MutableStateFlow(0)
    val remainingTime: StateFlow<Int> = _remainingTime

    private var timerJob: Job? = null

    fun startTimer(totalSeconds: Int, onFinish: () -> Unit) {
        stopTimer()
        _remainingTime.value = totalSeconds

        timerJob = viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value--
            }
            onFinish()
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }
}
