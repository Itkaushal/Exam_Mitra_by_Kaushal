package com.example.exammitrabykaushal.UIScreens.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exammitrabykaushal.DataLayer.TestResult
import com.example.exammitrabykaushal.ViewModel.TestHistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestHistoryScreen(
    onBack: () -> Unit,
    viewModel: TestHistoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val historyList by viewModel.historyList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Test History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->

        if (historyList.isEmpty()) {
            EmptyHistoryUI(padding)
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = historyList,
                    key = { it.id }
                ) { result ->
                    SwipeableHistoryItem(
                        result = result,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableHistoryItem(
    result: TestResult,
    viewModel: TestHistoryViewModel
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                viewModel.deleteHistory(result)
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) {
        HistoryItemCard(result, viewModel)
    }
}

@Composable
fun HistoryItemCard(
    result: TestResult,
    viewModel: TestHistoryViewModel
) {
    val bestResult by viewModel
        .getBestScore(result.testName)
        .collectAsState(initial = null)

    val percentage =
        if (result.totalQuestions > 0)
            (result.score * 100) / result.totalQuestions
        else 0

    val statusColor = when {
        percentage >= 80 -> Color(0xFF43A047)
        percentage >= 50 -> Color(0xFFFF9800)
        else -> Color(0xFFE53935)
    }

    val dateFormat = remember {
        SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    }

    Card(
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                color = statusColor.copy(alpha = 0.15f),
                shape = CircleShape,
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "$percentage%",
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        result.testName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    if (bestResult?.id == result.id) {
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            Icons.Default.EmojiEvents,
                            contentDescription = "Best Score",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    dateFormat.format(Date(result.date)),
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Text(
                    "✔ ${result.correctCount}   ✖ ${result.wrongCount}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${result.score}/${result.totalQuestions}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    if (percentage >= 50) "Passed" else "Failed",
                    color = statusColor,
                    fontSize = 12.sp
                )
                Text(
                    "⏱ ${result.timeTakenSeconds / 60} min",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun EmptyHistoryUI(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Face,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(60.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text("No tests taken yet!", color = Color.Gray)
        }
    }
}
