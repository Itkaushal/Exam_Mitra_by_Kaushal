package com.example.exammitrabykaushal.ViewModel


import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.example.exammitrabykaushal.model.TestQuestion
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class CreateTestViewModel : ViewModel() {

    /* ---------------- CONFIG SCREEN STATE ---------------- */

    // Question count must be Int (NOT List<TestQuestion>)
    private val _questionCount = MutableStateFlow(10)
    val questionCount: StateFlow<Int> = _questionCount

    fun updateQuestionCount(value: Int) {
        _questionCount.value = value
    }

    fun calculateTimeMinutes(toInt: Int): Int = _questionCount.value

    fun totalTimeSeconds(): Int = _questions.value.size * 60


    /* ---------------- QUESTIONS STATE ---------------- */

    private val _questions = MutableStateFlow<List<TestQuestion>>(emptyList())
    val questions: StateFlow<List<TestQuestion>> = _questions

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    /* ---------------- ANSWER STATE ---------------- */

    // questionIndex -> optionIndex
    private val _selectedAnswers = mutableStateMapOf<Int, Int>()
    val selectedAnswers: Map<Int, Int> = _selectedAnswers

    // questionIndex -> marked or not
    private val _markedForReview = mutableStateMapOf<Int, Boolean>()
    val markedForReview: Map<Int, Boolean> = _markedForReview

    /* ---------------- LOADING ---------------- */

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    /* ---------------- LOAD QUESTIONS FROM FIREBASE ---------------- */

    fun loadQuestions(category: String, limit: Int) {

        val node = when (category.lowercase()) {
            "math" -> "CreateTestMath"
            "science" -> "CreateTestScience"
            "gk" -> "CreateTestGk"
            "reasoning" -> "CreateTestReasoning"
            else -> "CreateTestMath"
        }

        _loading.value = true

        FirebaseDatabase.getInstance()
            .getReference(node)
            .limitToFirst(limit)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.children.mapNotNull {
                    it.getValue(TestQuestion::class.java)
                }

                _questions.value = list
                _currentIndex.value = 0
                _selectedAnswers.clear()
                _markedForReview.clear()
                _loading.value = false
            }
            .addOnFailureListener {
                _loading.value = false
            }
    }

    /* ---------------- USER ACTIONS ---------------- */

    fun selectAnswer(questionIndex: Int, optionIndex: Int) {
        _selectedAnswers[questionIndex] = optionIndex
    }

    fun toggleMarkForReview(questionIndex: Int) {
        _markedForReview[questionIndex] =
            !(_markedForReview[questionIndex] ?: false)
    }

    fun goTo(index: Int) {
        if (index in _questions.value.indices) {
            _currentIndex.value = index
        }
    }

    fun next() {
        if (_currentIndex.value < _questions.value.lastIndex) {
            _currentIndex.value++
        }
    }

    fun previous() {
        if (_currentIndex.value > 0) {
            _currentIndex.value--
        }
    }

    /* ---------------- RESULT CALCULATION ---------------- */

    fun calculateScore(): Int {
        return _questions.value.indices.count { index ->
            _selectedAnswers[index] == _questions.value[index].correctIndex
        }
    }
}


