package com.example.exammitrabykaushal.UIScreens.screen.testscreen


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exammitrabykaushal.ViewModel.CreateTestViewModel
import com.example.exammitrabykaushal.ViewModel.SectionalTestTimerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(
    category: String,
    totalQuestions: Int,
    onSubmit: (score: Int, attempted: Int, unAttempted: Int) -> Unit,
    onBack: () -> Unit,
    viewModel: CreateTestViewModel = viewModel(),
    timerViewModel: SectionalTestTimerViewModel = viewModel()
) {

    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val timeLeft by timerViewModel.remainingTime.collectAsState()
    val selectedAnswers = viewModel.selectedAnswers

    var showResult by remember { mutableStateOf(false) }

    /** Load questions */
    LaunchedEffect(category, totalQuestions) {
        viewModel.loadQuestions(category, totalQuestions)
    }

    /** Start timer AFTER questions load */
    LaunchedEffect(questions) {
        if (questions.isNotEmpty()) {
            timerViewModel.startTimer(viewModel.totalTimeSeconds()) {
                showResult = true // ⏱ auto submit
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { timerViewModel.stopTimer() }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category.uppercase(), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                actions = {
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                String.format("%02d:%02d", minutes, seconds),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    enabled = currentIndex > 0,
                    onClick = viewModel::previous,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Previous")
                }

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = {
                        if (currentIndex == questions.lastIndex) {
                            showResult = true
                        } else {
                            viewModel.next()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        if (currentIndex == questions.lastIndex) "Submit" else "Next",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { padding ->

        if (questions.isEmpty()) {
            Box(Modifier
                .fillMaxSize(),
                contentAlignment = Alignment.Center,
                ) {
                Text("Loading Questions...", color = Color.Gray, textAlign = TextAlign.Center)
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val question = questions[currentIndex]

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            LinearProgressIndicator(
                progress = (currentIndex + 1) / questions.size.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Question ${currentIndex + 1} of ${questions.size}",
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(Modifier.padding(16.dp)) {

                    Text(
                        question.text,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(16.dp))

                    question.options.forEachIndexed { index, option ->
                        val isSelected = selectedAnswers[currentIndex] == index

                        OutlinedButton(
                            onClick = {
                                viewModel.selectAnswer(currentIndex, index)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.surface
                            ),
                            border = if (isSelected)
                                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                            else null
                        ) {
                            Text(option)
                        }
                    }
                }
            }
        }
    }

    /** RESULT REDIRECTION */
    if (showResult) {
        timerViewModel.stopTimer()

        val score = viewModel.calculateScore()
        val attempted = viewModel.selectedAnswers.size
        val unAttempted = questions.size - attempted

        onSubmit(score, attempted, unAttempted)
    }
}



