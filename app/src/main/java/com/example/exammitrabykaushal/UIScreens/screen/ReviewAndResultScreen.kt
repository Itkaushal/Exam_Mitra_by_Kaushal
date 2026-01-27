package com.example.exammitrabykaushal.UIScreens.screen

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exammitrabykaushal.UIScreens.card.ReviewQuestionCard
import com.example.exammitrabykaushal.UIScreens.component.ConfettiAnimation
import com.example.exammitrabykaushal.model.Question

// Review & Result with Confetti
@Composable
fun ReviewAndResultScreen(
    questions: List<Question>,
    userAnswers: Map<String, Int>,
    score: Int,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    val attempted = userAnswers.size
    val unAttempted = questions.size - attempted
    val accuracy = if (attempted == 0) 0 else (score * 100) / attempted

    // Mock analytics (you can replace with Firebase later)
    val percentile = (70 + score).coerceAtMost(99)
    val rank = (5000 - score * 37).coerceAtLeast(1)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
    ) {
        ConfettiAnimation()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            /* ---------- HEADER ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Test Completed 🎉",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Row {
                    OutlinedButton(onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(width = 1.dp, color = Color.Cyan.copy(alpha = 0.4f))) { Text("Retry") }
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton(onClick = onBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Cyan.copy(alpha = 0.3f),
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(width = 1.dp, color = Color.Cyan.copy(alpha = 0.4f))) { Text("Dashboard") }
                }
            }

            Spacer(Modifier.height(16.dp))

            /* ---------- SCORE CARD ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFE3F2FD),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Your Score", fontSize = 16.sp, color = Color.Gray)
                    Text(
                        "$score / ${questions.size}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            /* ---------- ANALYTICS ---------- */
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnalyticsChip("Attempted", attempted.toString(), Color(0xFF1E88E5))
                AnalyticsChip("Accuracy", "$accuracy%", Color(0xFF43A047))
                AnalyticsChip("Unattempted", unAttempted.toString(), Color(0xFFE53935))
            }

            Spacer(Modifier.height(16.dp))

            /* ---------- RANK & PERCENTILE ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RankBox("Rank", "#$rank")
                RankBox("Percentile", "$percentile%")
            }

            Spacer(Modifier.height(20.dp))

            /* ---------- PERFORMANCE GRAPH ---------- */
            Text(
                "Performance Overview",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(8.dp))
            PerformanceBar(score, attempted, unAttempted)

            Spacer(Modifier.height(16.dp))

            /* ---------- REVIEW QUESTIONS ---------- */
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
fun AnalyticsChip(title: String, value: String, color: Color) {
    AssistChip(
        onClick = {},
        label = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(value, fontWeight = FontWeight.Bold)
                Text(title, fontSize = 12.sp)
            }
        },
        colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
            containerColor = color.copy(alpha = 0.15f),
            labelColor = color
        )
    )
}

@Composable
fun RankBox(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                Color(0xFFF5F5F5),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .width(140.dp)
    ) {
        Text(title, fontSize = 14.sp, color = Color.Gray)
        Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}


@Composable
fun PerformanceBar(correct: Int, attempted: Int, unAttempted: Int) {
    val total = correct + (attempted - correct) + unAttempted

    Column {
        BarItem("Correct", correct, total, Color(0xFF4CAF50))
        BarItem("Wrong", attempted - correct, total, Color(0xFFF44336))
        BarItem("Skipped", unAttempted, total, Color(0xFF9E9E9E))
    }
}

@Composable
fun BarItem(label: String, value: Int, total: Int, color: Color) {
    val fraction = if (total == 0) 0f else value / total.toFloat()

    Column {
        Text("$label : $value")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(Color.LightGray, RoundedCornerShape(10.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .height(10.dp)
                    .background(color, RoundedCornerShape(10.dp))
            )
        }
        Spacer(Modifier.height(6.dp))
    }
}

