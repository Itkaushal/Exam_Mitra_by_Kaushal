package com.example.exammitrabykaushal.UIScreens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exammitrabykaushal.ViewModel.QuizViewModel

// ---------------- QUIZ SCREEN ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    category: String,
    viewModel: QuizViewModel = viewModel(), // Standard ViewModel factory
    onBack: () -> Unit
) {
    // Load questions when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadQuestions(category)
    }

    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val isFinished by viewModel.isFinished.collectAsState()
    val score by viewModel.score.collectAsState()

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading or No Questions Found...")
        }
        return
    }

    // Result Screen Logic
    if (isFinished) {
        ResultScreen(score = score, total = questions.size, onBack = onBack)
    } else {
        // Test Taking Screen
        val currentQuestion = questions[currentIndex]

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("$category Test") },
                    navigationIcon = {
                        IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack,
                            contentDescription = "Back") }
                    }
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { viewModel.previousQuestion() },
                        enabled = currentIndex > 0
                    ) { Text("Previous") }

                    Button(
                        onClick = { viewModel.nextQuestion() }
                    ) {
                        Text(if (currentIndex == questions.size - 1) "Submit Test" else "Next")
                    }
                }
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                // Progress
                LinearProgressIndicator(
                    progress = (currentIndex + 1).toFloat() / questions.size,
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Question ${currentIndex + 1} of ${questions.size}", color = Color.Gray)

                Spacer(modifier = Modifier.height(24.dp))

                // Question
                Text(currentQuestion.text, style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(24.dp))

                // Options
                currentQuestion.options.forEachIndexed { index, option ->
                    val isSelected = userAnswers[currentQuestion.id] == index
                    OptionCard(
                        text = option,
                        isSelected = isSelected,
                        onClick = { viewModel.selectOption(currentQuestion.id, index) }
                    )
                }
            }
        }
    }
}

@Composable
fun OptionCard(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = isSelected, onClick = { onClick() })
            Text(text, modifier = Modifier.padding(start = 8.dp), fontSize = 16.sp)
        }
    }
}

@Composable
fun ResultScreen(score: Int, total: Int, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Face, contentDescription = null, modifier = Modifier.size(100.dp),
            tint = Color(0xFFFFD700))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Test Submitted!", style = MaterialTheme.typography.headlineMedium)
        Text("Your Score: $score / $total", style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack) {
            Text("Return to Home")
        }
    }
}