package com.example.exammitrabykaushal.model

data class TestQuestion(
    val id: Int = 0,
    val text: String = "",
    val options: List<String> = emptyList(),
    val correctIndex: Int = 0,
    val explanation: String = ""
)
