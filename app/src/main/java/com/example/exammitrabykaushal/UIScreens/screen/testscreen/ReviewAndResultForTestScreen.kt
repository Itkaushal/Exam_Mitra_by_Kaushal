package com.example.exammitrabykaushal.UIScreens.screen.testscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exammitrabykaushal.UIScreens.component.ConfettiAnimation
import com.example.exammitrabykaushal.UIScreens.screen.AnalyticsChip
import com.example.exammitrabykaushal.UIScreens.screen.PerformanceBar
import com.example.exammitrabykaushal.UIScreens.screen.RankBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewAndResultForTestScreen(
    score: Int,
    attempted: Int,
    unAttempted: Int,
    onBackToDashboard: () -> Unit,
    onRetry: () -> Unit,
    onReviewQuestions: () -> Unit = {} // 🔥 review callback
) {
    val total = attempted + unAttempted
    val accuracy = if (attempted == 0) 0 else (score * 100) / attempted
    val percentile = (70 + score).coerceAtMost(99)
    val rank = (5000 - score * 37).coerceAtLeast(1)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "RESULT",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackToDashboard) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        BoxWithConstraints(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            val isTablet = maxWidth > 600.dp
            val horizontalPadding = if (isTablet) 32.dp else 16.dp
            val scoreTextSize = if (isTablet) 44.sp else 34.sp

            ConfettiAnimation()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontalPadding),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                /* ---------- HEADER ---------- */
                Text(
                    "Test Completed 🎉",
                    fontSize = if (isTablet) 30.sp else 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Great effort! Keep improving 🚀",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )

                /* ---------- SCORE CARD ---------- */
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Your Score")
                        Text(
                            "$score / $total",
                            fontSize = scoreTextSize,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text("Accuracy $accuracy%")
                    }
                }

                /* ---------- ANALYTICS ---------- */
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AnalyticsChip("Attempted", attempted.toString(), MaterialTheme.colorScheme.primary)
                    AnalyticsChip("Accuracy", "$accuracy%", MaterialTheme.colorScheme.tertiary)
                    AnalyticsChip("Skipped", unAttempted.toString(), MaterialTheme.colorScheme.error)
                }

                /* ---------- RANK ---------- */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RankBox("Rank", "#$rank")
                    RankBox("Percentile", "$percentile%")
                }

                /* ---------- PERFORMANCE ---------- */
                Text(
                    "Performance Overview",
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isTablet) 22.sp else 18.sp
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        PerformanceBar(
                            correct = score,
                            attempted = attempted,
                            unAttempted = unAttempted
                        )
                    }
                }

                /* ---------- REVIEW SECTION ---------- */
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Review Your Answers",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Text(
                            "Check correct, wrong and skipped questions in detail.",
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(0.7f)
                        )

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onReviewQuestions,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1565C0),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Review Questions")
                        }
                    }
                }

                /* ---------- ACTION BUTTONS ---------- */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onRetry
                    ) {
                        Text("Retry Test")
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onBackToDashboard,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1565C0),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Go Home")
                    }
                }
            }
        }
    }
}
