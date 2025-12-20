/*
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
}*/

package com.example.exammitrabykaushal.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// --------- Question model used by the screen ----------
data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String = "" // explanation shown in review
)

// --------- Simple ViewModel (in-memory sample) ----------
class QuizViewModel : ViewModel() {

    // Questions list
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    // user's selected answers: map questionId -> selectedOptionIndex
    private val _userAnswers = MutableStateFlow<Map<String, Int>>(emptyMap())
    val userAnswers: StateFlow<Map<String, Int>> = _userAnswers.asStateFlow()

    // current question index
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    // finished state
    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    // score (computed when finished)
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    // loadQuestions (dummy for now) — you can replace to fetch from repo
    fun loadQuestions(category: String) {
        // create dummy list (10 qns)
        val sample = (1..10).map { i ->
            Question(
                id = "q$i",
                text = "$category: Sample Question #$i - Identify the right answer",
                options = listOf("Option A", "Option B", "Option C", "Option D"),
                correctIndex = (i % 4),
                explanation = "Explanation for question #$i: Reasoning and tips to solve this question."
            )
        }
        _questions.value = sample
        _userAnswers.value = emptyMap()
        _currentQuestionIndex.value = 0
        _isFinished.value = false
        _score.value = 0
    }

    fun selectOption(questionId: String, optionIndex: Int) {
        val updated = _userAnswers.value.toMutableMap()
        updated[questionId] = optionIndex
        _userAnswers.value = updated
    }

    fun nextQuestion() {
        val max = _questions.value.size
        if (_currentQuestionIndex.value < max - 1) {
            _currentQuestionIndex.value += 1
        } else {
            finishQuiz()
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) _currentQuestionIndex.value -= 1
    }

    fun goToQuestion(index: Int) {
        if (index in 0 until _questions.value.size) _currentQuestionIndex.value = index
    }

    fun finishQuiz() {
        // compute score
        val qs = _questions.value
        var s = 0
        for (q in qs) {
            val sel = _userAnswers.value[q.id]
            if (sel != null && sel == q.correctIndex) s++
        }
        _score.value = s
        _isFinished.value = true
    }
}

