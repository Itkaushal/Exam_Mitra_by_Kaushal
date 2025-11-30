package com.example.exammitrabykaushal.DataLayer

data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val category: String // "Math", "Reasoning", "GS", "PYQ"
)

// Singleton Repository (Acts as your Database for now)
object QuestionRepository {
    fun getQuestionsByCategory(category: String): List<Question> {
        val allQuestions = listOf(
            // GS Questions
            Question(1, "Who is the President of India?",
                listOf("Draupadi Murmu", "Narendra Modi", "Amit Shah", "Rahul Gandhi"),
                0, "GS"),
            Question(2, "Capital of Australia?",
                listOf("Sydney", "Melbourne", "Canberra", "Perth"),
                2, "GS"),

            // Math Questions
            Question(3, "Find x: 2x + 4 = 10",
                listOf("2", "3", "4", "5"),
                1, "Math"),
            Question(4, "Square root of 144?",
                listOf("10", "11", "12", "13"),
                2, "Math"),

            // Reasoning
            Question(5, "Man is to Woman as Boy is to...?",
                listOf("Girl", "Child", "Adult", "Human"),
                0, "Reasoning")
        )

        // If category is "Mock", return mixed. Else return specific subject.
        return if (category == "Mock") allQuestions else allQuestions.filter { it.category == category }
    }
}