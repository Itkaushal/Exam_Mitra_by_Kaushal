package com.example.exammitrabykaushal.DataLayer.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val timeTakenSeconds: Int,
    val timestamp: Long,
    val totalMarks: Int,
)
