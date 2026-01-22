package com.example.exammitrabykaushal.UIScreens.screen

import android.R
import android.widget.Toast
import androidx.activity.compose.ReportDrawn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.OutlinedButtonDefaults
import com.example.exammitrabykaushal.UIScreens.card.OptionCardWithExplanation
import com.example.exammitrabykaushal.UIScreens.component.formatTime
import com.example.exammitrabykaushal.ViewModel.QuizViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    category: String,
    viewModel: QuizViewModel = viewModel(),
    onBack: () -> Unit
) {

    /* ---------------- CATEGORY → FIREBASE NODE ---------------- */
    val firebaseNode = remember(category) {
        when (category.lowercase()) {
            "full_length" -> "mock_test"
            "math" -> "math_questions"
            "reasoning" -> "reasoning_questions"
            "gs" -> "gs_questions"
            else -> category   // PYQ like rrb_je_2024
        }
    }

    /* ---------------- LOAD QUESTIONS ---------------- */
    LaunchedEffect(firebaseNode) {
        viewModel.loadQuestions(firebaseNode)
    }

    /* ---------------- STATE ---------------- */
    val questions by viewModel.questions.collectAsState()
    val idx by viewModel.currentQuestionIndex.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val isFinished by viewModel.isFinished.collectAsState()
    val score by viewModel.score.collectAsState()
    var showSubmitDialog by remember { mutableStateOf(false) }


    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    /* ---------------- TIMER ---------------- */
    val totalTime = 60 * 60
    var timeLeft by remember { mutableStateOf(totalTime) }
    var timerRunning by remember { mutableStateOf(true) }

    LaunchedEffect(timerRunning, isFinished) {
        while (timerRunning && timeLeft > 0 && !isFinished) {
            delay(1000)
            timeLeft--
        }
        if (timeLeft == 0 && !isFinished) {
            viewModel.finishQuiz()
        }
    }

    /* ---------------- LOADING STATE ---------------- */
    if (questions.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Loading Questions...", Toast.LENGTH_SHORT).show()
        }
        return
    }

    /* ---------------- RESULT SCREEN ---------------- */
    if (isFinished) {
        ReviewAndResultScreen(
            questions = questions,
            userAnswers = userAnswers,
            score = score,
            onBack = onBack,
            onRetry = {
                timeLeft = totalTime
                timerRunning = true
                viewModel.resetQuiz()
                viewModel.loadQuestions(firebaseNode)
            }
        )
        return
    }

    /* ---------------- SAFE CURRENT QUESTION ---------------- */
    val safeIndex = idx.coerceIn(0, questions.lastIndex)
    val currentQuestion = questions[safeIndex]

    /* ---------------- BOTTOM SHEET NAVIGATOR ---------------- */
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(Modifier.padding(16.dp)) {

                Text("Navigate Questions", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))

                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    questions.forEachIndexed { i, q ->
                        AssistChip(
                            onClick = {
                                viewModel.goToQuestion(i)
                                scope.launch {
                                    sheetState.hide()
                                    showSheet = false
                                }
                            },
                            label = { Text("${i + 1}") },
                            leadingIcon = {
                                Icon(
                                    imageVector = when {
                                        i == safeIndex -> Icons.Default.RadioButtonChecked
                                        userAnswers.containsKey(q.id) -> Icons.Default.CheckCircle
                                        else -> Icons.Default.RadioButtonUnchecked
                                    },
                                    contentDescription = null,
                                    tint = Color.Blue.copy(alpha = 0.4f)
                                )
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedButton(onClick = { viewModel.finishQuiz() },
                        border = BorderStroke(width = 1.dp, color = Color.Green.copy(alpha = 0.6f))) {
                        Text("Submit Test")
                    }
                    OutlinedButton(onClick = { showSheet = false },
                        border = BorderStroke(width = 1.dp, color = Color.Red.copy(alpha = 0.6f))) {
                        Text("Close")
                    }
                }
            }
        }
    }

    /* ---------------- MAIN UI ---------------- */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(firebaseNode.replace("_", " ").uppercase()) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showSheet = true
                        scope.launch { sheetState.show() }
                    }) {
                        Icon(Icons.Default.List, null)
                    }
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
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // -----Previous Button-----------
                OutlinedButton(
                    onClick = { viewModel.previousQuestion() },
                    enabled = safeIndex > 0
                ) { Text("Prev") }

                // -----Submit Button---------
                OutlinedButton(
                    onClick = { showSubmitDialog = true },
                    enabled = safeIndex == questions.lastIndex,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    border = BorderStroke(width = 1.dp, color = Color.Gray),
                ) {
                    Text("Submit Test")
                }

                if(showSubmitDialog){
                    AlertDialog(
                        onDismissRequest = {showSubmitDialog = false},
                        title = {Text("Submit Test")},
                        text = {
                            Text("Are you sure you want to submit the test before completion?")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showSubmitDialog = false
                                    viewModel.submitQuiz()
                                }
                            ) {
                                Text("Yes, Submit")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showSubmitDialog = false }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }


                Row {
                    // -----
                    OutlinedButton(onClick = { timerRunning = !timerRunning }) {
                        Icon(
                            if (timerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            null
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(if (timerRunning) "Pause" else "Resume")
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(onClick = {
                        if (safeIndex == questions.lastIndex)
                            viewModel.finishQuiz()
                        else
                            viewModel.nextQuestion()
                    }) {
                        Text(if (safeIndex == questions.lastIndex) "Submit" else "Next")
                    }
                }
            }
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            LinearProgressIndicator(
                progress = (safeIndex + 1f) / questions.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.height(8.dp))

            Text(
                formatTime(timeLeft),
                fontWeight = FontWeight.Bold,
                color = if (timeLeft < 60) Color.Red else Color.Red
            )

            Spacer(Modifier.height(12.dp))

            Text("Question ${safeIndex + 1} of ${questions.size}", color = Color.Gray)

            Spacer(Modifier.height(12.dp))

            Text(
                currentQuestion.text,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            currentQuestion.options.forEachIndexed { i, opt ->
                OptionCardWithExplanation(
                    text = opt,
                    isSelected = userAnswers[currentQuestion.id] == i,
                    onClick = { viewModel.selectOption(currentQuestion.id, i) },
                    showExplanation = false,
                    correctIndex = currentQuestion.correctIndex,
                    explanation = currentQuestion.explanation
                )
            }
        }
    }
}
