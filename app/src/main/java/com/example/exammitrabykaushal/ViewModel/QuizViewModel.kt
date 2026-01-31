package com.example.exammitrabykaushal.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exammitrabykaushal.model.Question
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class QuizViewModel : ViewModel() {

    /* ---------------- STATE ---------------- */

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    private val _userAnswers = MutableStateFlow<Map<String, Int>>(emptyMap())
    val userAnswers: StateFlow<Map<String, Int>> = _userAnswers

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished



    val score: StateFlow<Int> = _userAnswers
        .map { answers ->
            _questions.value.count { q ->
                answers[q.id] == q.correctIndex
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    /* ---------------- LOAD QUESTIONS (GENERIC) ---------------- */

    fun loadQuestions(firebaseNode: String) {
        val ref = FirebaseDatabase.getInstance()
            .getReference(firebaseNode)
        ref.get()
            .addOnSuccessListener { snapshot ->

                val list = snapshot.children.mapNotNull { qSnap ->
                    val id = qSnap.key ?: return@mapNotNull null
                    val text = qSnap.child("text").getValue(String::class.java) ?: return@mapNotNull null
                    val correctIndex = qSnap.child("correctIndex").getValue(Int::class.java) ?: return@mapNotNull null
                    val explanation = qSnap.child("explanation").getValue(String::class.java) ?: ""

                    val options = qSnap.child("options")
                        .children
                        .sortedBy { it.key?.toIntOrNull() ?: 0 }
                        .mapNotNull { it.getValue(String::class.java) }

                    if (options.isEmpty()) return@mapNotNull null

                    Question(id, text, options, correctIndex, explanation)
                }

                _questions.value = list
                resetQuiz()
            }
            .addOnFailureListener {
                Log.e("Firebase", "Error getting data", it)
            }

    }


    /* ---------------- USER ACTIONS ---------------- */

    fun selectOption(qId: String, index: Int) {
        _userAnswers.value = _userAnswers.value + (qId to index)
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.value < _questions.value.lastIndex) {
            _currentQuestionIndex.value++
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value--
        }
    }

    fun goToQuestion(index: Int) {
        if (index in _questions.value.indices) {
            _currentQuestionIndex.value = index
        }
    }

    fun finishQuiz() {
        _isFinished.value = true
    }


    /* ---------------- RESET (USED BY RETRY) ---------------- */

    fun resetQuiz() {
        _currentQuestionIndex.value = 0
        _userAnswers.value = emptyMap()
        _isFinished.value = false
    }
}
