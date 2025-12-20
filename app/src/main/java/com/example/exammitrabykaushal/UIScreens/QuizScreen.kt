/*
package com.example.exammitrabykaushal.UIScreens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exammitrabykaushal.ViewModel.QuizViewModel
import kotlinx.coroutines.delay

// ---------------- QUIZ SCREEN ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    category: String,
    viewModel: QuizViewModel = viewModel(),
    onBack: () -> Unit
) {
    // Load questions
    LaunchedEffect(Unit) {
        viewModel.loadQuestions(category)
    }

    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val isFinished by viewModel.isFinished.collectAsState()
    val score by viewModel.score.collectAsState()

    // -------------------- TIMER ------------------------
    var timeLeft by remember { mutableStateOf(15 * 60) } // 15 min = 900 sec

    LaunchedEffect(key1 = currentIndex) {
        // Start timer only when quiz begins
        while (timeLeft > 0 && !isFinished) {
            delay(1000)
            timeLeft--
        }
        if (timeLeft == 0) {
            viewModel.submitTest() // auto submit on timeout
        }
    }

    fun formatTime(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return "%02d:%02d".format(m, s)
    }

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // -------------------- RESULT SCREEN ------------------------
    if (isFinished) {
        ResultScreen(
            score = score,
            total = questions.size,
            onBack = onBack
        )
        return
    }

    val currentQuestion = questions[currentIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$category Test") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { viewModel.previousQuestion() },
                    enabled = currentIndex > 0
                ) {
                    Text("Previous")
                }

                Button(
                    onClick = {
                        if (currentIndex == questions.size - 1) {
                            viewModel.submitTest()
                        } else {
                            viewModel.nextQuestion()
                        }
                    }
                ) {
                    Text(if (currentIndex == questions.size - 1) "Submit" else "Next")
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // -------------------- Timer + Progress Bar ------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LinearProgressIndicator(
                    progress = (currentIndex + 1).toFloat() / questions.size,
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = formatTime(timeLeft),
                    fontWeight = FontWeight.Bold,
                    color = if (timeLeft < 60) Color.Red else MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Question ${currentIndex + 1} of ${questions.size}",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // -------------------- Question Text ------------------------
            Text(
                currentQuestion.text,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // -------------------- Options ------------------------
            currentQuestion.options.forEachIndexed { index, option ->
                val isSelected = userAnswers[currentQuestion.id] == index
                OptionCard(
                    text = option,
                    isSelected = isSelected,
                    onClick = {
                        viewModel.selectOption(currentQuestion.id, index)
                    }
                )
            }
        }
    }
}


// -------------------- OPTION CARD ------------------------
@Composable
fun OptionCard(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor =
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = { onClick() }
            )
            Text(
                text = text,
                modifier = Modifier.padding(start = 12.dp),
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


// -------------------- RESULT SCREEN ------------------------
@Composable
fun ResultScreen(score: Int, total: Int, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Mock Test Finished!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Your Score",
            fontSize = 18.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            "$score / $total",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = onBack) {
            Text("Return to Home")
        }
    }
}
*/

package com.example.exammitrabykaushal.UIScreens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exammitrabykaushal.ViewModel.Question
import com.example.exammitrabykaushal.ViewModel.QuizViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    category: String,
    viewModel: QuizViewModel = viewModel(),
    onBack: () -> Unit
) {
    // load questions once
    LaunchedEffect(Unit) { viewModel.loadQuestions(category) }

    val questions by viewModel.questions.collectAsState()
    val idx by viewModel.currentQuestionIndex.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val isFinished by viewModel.isFinished.collectAsState()
    val score by viewModel.score.collectAsState()

    // Theme Colors
    val BluePrimary = Color(0xFF1565C0)

    // TIMER
    var timeLeft by remember { mutableStateOf(15 * 60) } // seconds
    val timerRunning = remember { mutableStateOf(true) }

    // Timer coroutine
    LaunchedEffect(timerRunning.value) {
        while (timerRunning.value && timeLeft > 0 && !isFinished) {
            delay(1000)
            timeLeft--
        }
        if (timeLeft == 0 && !isFinished) {
            viewModel.finishQuiz()
        }
    }

    // Bottom sheet state for navigator
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()

    // show/hide sheet flag (prevents blocking)
    var showSheet by remember { mutableStateOf(false) }

    // Use system dark theme check (if you want further color tweaks)
    val isDark = isSystemInDarkTheme()

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // If finished -> show review + confetti
    if (isFinished) {
        ReviewAndResultScreen(
            questions = questions,
            userAnswers = userAnswers,
            score = score,
            onBack = onBack,
            onRetry = {
                viewModel.loadQuestions(category)
                timeLeft = 15 * 60
                timerRunning.value = true
            }
        )
        return
    }

    val currentQuestion = questions[idx]

    // Only render sheet when asked to (avoids blocking touches)
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            dragHandle = { Spacer(modifier = Modifier.height(6.dp)) }
        ) {
            // Bottom content: question navigator grid (1..N)
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Navigate Questions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black)
                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    questions.forEachIndexed { i, _ ->
                        val answered = userAnswers.containsKey(questions[i].id)
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
                                    imageVector = if (i == idx) Icons.Default.RadioButtonChecked else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.Red,

                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = when {
                                    i == idx -> Color.Unspecified
                                    answered -> Color(0xFFe8f5e9) // light green for answered
                                    else -> Color.Unspecified
                                }
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = {
                        viewModel.finishQuiz()
                        scope.launch {
                            sheetState.hide()
                            showSheet = false
                        } },
                        border = BorderStroke(width = 1.dp, color = Color.Blue),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                        ) { Text("Submit Test") }

                    OutlinedButton(onClick = { scope.launch { sheetState.hide(); showSheet = false } },
                        border = BorderStroke(width = 1.dp, color = Color.Red.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Black,
                            containerColor = Color.White
                        )) {
                        Text("Close")
                    }
                }
            }
        }
    }

    // Main quiz UI
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$category Test") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        showSheet = true
                        scope.launch { sheetState.show() }
                    }) {
                        Icon(Icons.Default.List, contentDescription = "Navigator")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = { viewModel.previousQuestion() }, enabled = idx > 0,
                    border = BorderStroke(width = 1.dp, color = Color.Gray.copy(alpha = 0.4f))) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Prev")
                }
                Row {
                    OutlinedButton(onClick = { timerRunning.value = !timerRunning.value },
                        border = BorderStroke(width = 1.dp, color = Color.Red),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Black,
                            containerColor = Color.White
                        )) {
                        Icon(
                            imageVector = if (timerRunning.value) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(if (timerRunning.value) "Pause" else "Resume")
                    }

                    Spacer(modifier = Modifier.size(8.dp))

                    Button(onClick = { if (idx == questions.size - 1) viewModel.finishQuiz() else viewModel.nextQuestion() },
                        border = BorderStroke(width = 1.dp, color = Color.Blue),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)) {
                        Text(if (idx == questions.size - 1) "Submit" else "Next")
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Progress + timer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    color = Color.Red.copy(alpha = 0.7f),
                    progress = (idx + 1).toFloat() / questions.size,
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(8.dp)),
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = formatTime(timeLeft),
                    fontWeight = FontWeight.Bold,
                    color = if (timeLeft < 60) Color.Red else Color.Blue.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Question ${idx + 1} of ${questions.size}", color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                currentQuestion.text,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.height(18.dp))

            // Options list
            currentQuestion.options.forEachIndexed { i, opt ->
                val selected = userAnswers[currentQuestion.id] == i
                OptionCardWithExplanation(
                    text = opt,
                    isSelected = selected,
                    onClick = { viewModel.selectOption(currentQuestion.id, i) },
                    showExplanation = false,
                    correctIndex = currentQuestion.correctIndex,
                    explanation = currentQuestion.explanation
                )
            }
        }
    }
}

// -----------------------------
// Review & Result with Confetti
// -----------------------------
@Composable
private fun ReviewAndResultScreen(
    questions: List<Question>,
    userAnswers: Map<String, Int>,
    score: Int,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(top = 40.dp)) {
        // confetti behind the content
        ConfettiAnimation()

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Test Completed!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Score: $score / ${questions.size}", color = MaterialTheme.colorScheme.primary)
                }
                Row {
                    OutlinedButton(onClick = onRetry) { Text("Retry") }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color.Blue.copy(alpha = 0.5f)
                        ),
                        border = BorderStroke(width = 1.dp, color = Color.Blue)
                    ) { Text("Home") }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Review list - each question shows user answer, correct answer, explanation
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
                items(questions.size) { idx ->
                    val q = questions[idx]
                    val user = userAnswers[q.id]
                    ReviewQuestionCard(q, user)
                }
            }
        }
    }
}

@Composable
private fun ReviewQuestionCard(q: Question, userSelected: Int?) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(q.text, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            q.options.forEachIndexed { i, opt ->
                val isCorrect = i == q.correctIndex
                val isUser = userSelected == i
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .border(
                                width = 1.dp,
                                color = if (isCorrect) Color.Green else if (isUser) Color.Blue else Color.LightGray,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${'A' + i}", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        opt,
                        color = when {
                            isCorrect -> Color(0xFF0F9D58)
                            isUser -> MaterialTheme.colorScheme.primary
                            else -> Color.Unspecified
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Explanation
            Text("Explanation:", fontWeight = FontWeight.Medium)
            Text(q.explanation, color = Color.Gray)
        }
    }
}

// Option card reused but with explanation param (we hide explanation during test; displayed in review)
@Composable
fun OptionCardWithExplanation(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    showExplanation: Boolean,
    correctIndex: Int,
    explanation: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
            .border(width = if (isSelected) 2.dp else 1.dp, color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = isSelected, onClick = onClick)
                Spacer(modifier = Modifier.size(12.dp))
                Text(text, fontSize = 16.sp)
            }
            if (showExplanation) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(explanation, color = Color.Gray, modifier = Modifier.padding(12.dp))
            }
        }
    }
}

// -----------------------------
// Confetti (lightweight, purely Compose)
// -----------------------------
@Composable
fun ConfettiAnimation(count: Int = 30) {
    val density = LocalDensity.current
    val widthPx = with(density) { 360.dp.toPx() } // approx - the canvas will scale
    val heightPx = with(density) { 600.dp.toPx() }

    // particles data
    val particles = remember {
        List(count) {
            Particle(
                x = Random.nextFloat() * widthPx,
                y = -Random.nextFloat() * 200f,
                vx = (Random.nextFloat() - 0.5f) * 80f,
                vy = Random.nextFloat() * 200f + 100f,
                size = Random.nextFloat() * 12f + 6f,
                color = listOf(Color(0xFFE53935), Color(0xFFFB8C00), Color(0xFFFFEB3B), Color(0xFF43A047), Color(0xFF1E88E5)).random(),
                rotation = Random.nextFloat() * 360f,
                rotSpeed = (Random.nextFloat() - 0.5f) * 200f
            )
        }
    }

    // animated state
    val elapsed = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        elapsed.animateTo(1f, animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing)))
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val t = elapsed.value
        for (p in particles) {
            val nx = p.x + p.vx * t
            val ny = p.y + p.vy * t + 0.5f * 300f * t * t // gravity-ish
            rotate(p.rotation + p.rotSpeed * t, pivot = Offset(nx, ny)) {
                drawRect(color = p.color, topLeft = Offset(nx, ny), size = androidx.compose.ui.geometry.Size(p.size, p.size))
            }
        }
    }
}

private data class Particle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val size: Float,
    val color: Color,
    val rotation: Float,
    val rotSpeed: Float
)

// -----------------------------
// Helpers
// -----------------------------
fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}


