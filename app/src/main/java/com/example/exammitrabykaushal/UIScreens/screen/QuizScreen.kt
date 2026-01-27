package com.example.exammitrabykaushal.UIScreens.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exammitrabykaushal.DataLayer.Entity.TestResult
import com.example.exammitrabykaushal.UIScreens.card.OptionCardWithExplanation
import com.example.exammitrabykaushal.UIScreens.component.formatTime
import com.example.exammitrabykaushal.ViewModel.QuizViewModel
import com.example.exammitrabykaushal.ViewModel.TestHistoryViewModel
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
            CircularProgressIndicator(
                color = Color.Blue,
                strokeCap = StrokeCap.Square,
                trackColor = Color.LightGray,
                modifier = Modifier.size(48.dp)
            )
        }
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Loading Questions...", Toast.LENGTH_SHORT).show()
        }
        return
    }

    /* ---------------- RESULT SCREEN ---------------- */
    val historyViewModel : TestHistoryViewModel = viewModel()

    LaunchedEffect(isFinished) {
        if (isFinished) {

            val timeTaken = totalTime - timeLeft
            val correctCount = score
            val wrongCount = questions.size - score

            val timeStamp = System.currentTimeMillis()
            val totalMarks = questions.size * 100


            historyViewModel.insertResult(
                TestResult(
                    testName = firebaseNode.replace("_", " ").uppercase(),
                    score = score,
                    totalQuestions = questions.size,
                    correctCount = correctCount,
                    wrongCount = wrongCount,
                    timeTakenSeconds = timeTaken,

                    timestamp = timeStamp,
                    totalMarks = totalMarks
                )
            )
        }
    }


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
            sheetState = sheetState,
        ) {

            Column(
                modifier = Modifier
                    .fillMaxHeight(0.9f) // important
                    .padding(16.dp)
            ) {

                /* -------- TITLE -------- */
                Text(
                    text = "Navigate Questions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                /* -------- SCROLLABLE QUESTION GRID -------- */
                Box(
                    modifier = Modifier
                        .weight(1f) // takes remaining space
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(questions) { i, q ->
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
                                        tint = Color.Blue.copy(alpha = 0.6f)
                                    )
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                /* -------- FIXED ACTION BUTTONS -------- */
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    OutlinedButton(
                        onClick = { viewModel.finishQuiz() },
                        border = BorderStroke(1.dp, Color.Green),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green.copy(alpha = 0.05f),
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Submit Test")
                    }

                    OutlinedButton(
                        onClick = { showSheet = false },
                        border = BorderStroke(1.dp, Color.Red),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.03f),
                            contentColor = Color.Black
                        )
                    ) {
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
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // -----Previous Button-----------
                OutlinedButton(
                    onClick = { viewModel.previousQuestion() },
                    enabled = safeIndex > 0,
                    border = BorderStroke(width = 1.dp, color = Color.Gray),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Prev",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                        textAlign = TextAlign.Center,
                        letterSpacing = 1.sp))
                }

                Spacer(Modifier.width(8.dp))
                // Pause Resume Button
                OutlinedButton(onClick = { timerRunning = !timerRunning },
                    border = BorderStroke(width = 1.dp, color = Color.Gray),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (timerRunning) Color.White else Color.Red.copy(alpha = 0.01f),
                    ),
                ) {
                    Icon(
                        if (timerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        null,
                        tint = if (timerRunning) Color.Gray else Color.LightGray
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(if (timerRunning) "Pause" else "Resume",
                        style = TextStyle(
                            color = if (timerRunning) Color.Gray else Color.LightGray,
                            fontSize = 16.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                            textAlign = TextAlign.Center,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Spacer(Modifier.width(8.dp))
                // Next or Submit Button
                OutlinedButton(onClick = {
                    if (safeIndex == questions.lastIndex)
                        viewModel.finishQuiz()
                    else
                        viewModel.nextQuestion() },
                    border = BorderStroke(width = 1.dp, color = Color.White),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue.copy(alpha = 0.7f),
                        ),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Text(if (safeIndex == questions.lastIndex) "Submit" else "Next",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                            textAlign = TextAlign.Center,
                            letterSpacing = 1.sp
                        ))
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
