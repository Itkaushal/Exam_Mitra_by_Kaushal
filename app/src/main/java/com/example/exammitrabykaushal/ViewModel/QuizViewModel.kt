package com.example.exammitrabykaushal.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel // Change 1: Use AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.exammitrabykaushal.DataLayer.AppDatabase
import com.example.exammitrabykaushal.DataLayer.Question
import com.example.exammitrabykaushal.DataLayer.QuestionRepository
import com.example.exammitrabykaushal.DataLayer.TestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Change 2: Inherit from AndroidViewModel to get Application Context
class QuizViewModel(application: Application) : AndroidViewModel(application) {

    // State
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions = _questions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex = _currentQuestionIndex.asStateFlow()

    private val _userAnswers = MutableStateFlow<MutableMap<Int, Int>>(mutableMapOf())
    val userAnswers = _userAnswers.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished = _isFinished.asStateFlow()

    // Change 3: Variable to remember the test name
    private var currentTestName: String = ""

    // Load data based on what the user clicked
    fun loadQuestions(category: String) {
        currentTestName = category // Save the name for later

        // Reset state
        _currentQuestionIndex.value = 0
        _userAnswers.value = mutableMapOf()
        _isFinished.value = false
        _score.value = 0

        // Fetch from Singleton Repository
        _questions.value = QuestionRepository.getQuestionsByCategory(category)
    }

    fun selectOption(questionId: Int, optionIndex: Int) {
        if (_isFinished.value) return
        val newMap = _userAnswers.value.toMutableMap()
        newMap[questionId] = optionIndex
        _userAnswers.value = newMap
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.value < _questions.value.size - 1) {
            _currentQuestionIndex.value += 1
        } else {
            // Change 4: Now we can call this without passing arguments
            submitTest()
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value -= 1
        }
    }

    // Change 5: Logic to calculate score AND save to DB
    fun submitTest() {
        if (_isFinished.value) return // Prevent double submission

        var tempScore = 0
        _questions.value.forEach { q ->
            if (_userAnswers.value[q.id] == q.correctOptionIndex) {
                tempScore++
            }
        }

        _score.value = tempScore

        // SAVE TO DB
        // Use 'getApplication()' to get Context inside AndroidViewModel
        val context = getApplication<Application>().applicationContext
        val totalQ = _questions.value.size
        val testName = currentTestName

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            db.historyDao().insertResult(
                TestResult(
                    testName = testName,
                    score = tempScore,
                    totalQuestions = totalQ
                )
            )
        }

        _isFinished.value = true
    }
}