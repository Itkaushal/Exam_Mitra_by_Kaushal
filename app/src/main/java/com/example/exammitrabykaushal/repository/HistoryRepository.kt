package com.example.exammitrabykaushal.repository

import com.example.exammitrabykaushal.DataLayer.HistoryDao
import com.example.exammitrabykaushal.DataLayer.TestResult
import kotlinx.coroutines.flow.Flow

class HistoryRepository (private  val dao: HistoryDao) {

    val allHistory = dao.getAllHistory()

    suspend fun insertResult(result : TestResult){
        dao.insertResult(result)
    }

    fun getBestScore(testName: String) : Flow<TestResult?> {
        return dao.getBestScore(testName)
    }
}