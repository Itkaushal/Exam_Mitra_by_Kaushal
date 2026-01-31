package com.example.exammitrabykaushal.UIScreens.screen

import com.example.exammitrabykaushal.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data class to hold Exam Details
data class ExamCategory(
    val name: String,
    val icon: Painter,
    val colorStart: Color,
    val colorEnd: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PYQSelectionScreen(
    onExamSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    // Theme Colors
    val BluePrimary = Color(0xFF1565C0)

    // List of Exams
    val examList = listOf(
        ExamCategory("UPSC CSE", painterResource(id = R.drawable.statevideo), Color(0xFF5C6BC0), Color(0xFF3949AB)),
        ExamCategory("SSC CGL", painterResource(id = R.drawable.upscvideo), Color(0xFF66BB6A), Color(0xFF43A047)),
        ExamCategory("SSC CHSL/MTS", painterResource(id = R.drawable.sscvideo), Color(0xFFFFA726), Color(0xFFFB8C00)),
        ExamCategory("SSC JE", painterResource(id = R.drawable.sscvideo), Color(0xFFEC407A), Color(0xFFD81B60)),
        ExamCategory("RRB NTPC", painterResource(id = R.drawable.trainvideo), Color(0xFF29B6F6), Color(0xFF039BE5)),
        ExamCategory("RRB Group D", painterResource(id = R.drawable.trainvideo), Color(0xFF7E57C2), Color(0xFF5E35B1)),
        ExamCategory("State PSC", painterResource(id = R.drawable.statevideo), Color(0xFFFF7043), Color(0xFFF4511E)),
        ExamCategory("Banking/IBPS", painterResource(id = R.drawable.bankinvideo), Color(0xFF26A69A), Color(0xFF00897B))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Exam Category", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Section
            Text(
                "Previous Year Papers",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                "Practice with actual questions from past exams.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Grid of Exam Cards
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 Columns
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(examList) { exam ->
                    ExamCard(exam = exam, onClick = { onExamSelected(exam.name) })
                }
            }
        }
    }
}

@Composable
fun ExamCard(exam: ExamCategory, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp) // Taller card for better look
            .shadow(6.dp, RoundedCornerShape(24.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Important for gradient
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(exam.colorStart, exam.colorEnd)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // White Circle Background for Logo
                // This makes the logo pop out against the colored gradient
                Surface(
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.9f),
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = exam.icon,
                            contentDescription = null,
                            tint = Color.Unspecified, // Shows original logo colors
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Text
                Text(
                    text = exam.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }
    }
}