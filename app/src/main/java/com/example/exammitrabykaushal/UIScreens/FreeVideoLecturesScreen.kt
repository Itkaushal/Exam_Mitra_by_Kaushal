package com.example.exammitrabykaushal.UIScreens

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
import androidx.compose.ui.draw.clip
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
data class SubjectCategory(
    val name: String,
    val icon: Painter,
    val colorStart: Color,
    val colorEnd: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreeVideoLecturesScreen(
    onSubjectSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    // Theme Colors
    val BluePrimary = Color(0xFF1565C0)

    // List of Exams
    val subjectList = listOf(
        SubjectCategory("Maths", painterResource(id = R.drawable.mathvideo), Color(0xFF5C6BC0), Color(0xFF3949AB)),
        SubjectCategory("Reasoning", painterResource(id = R.drawable.reasoningvideo), Color(0xFF66BB6A), Color(0xFF43A047)),
        SubjectCategory("GK/GS", painterResource(id = R.drawable.gkgsvideo), Color(0xFFFFA726), Color(0xFFFB8C00)),
        SubjectCategory("English", painterResource(id = R.drawable.english), Color(0xFFEC407A), Color(0xFFD81B60)),
        SubjectCategory("Hindi", painterResource(id = R.drawable.hindivideo), Color(0xFF29B6F6), Color(0xFF039BE5)),
        SubjectCategory("Science", painterResource(id = R.drawable.sciencevideo), Color(0xFF7E57C2), Color(0xFF5E35B1)),
        SubjectCategory("Computer", painterResource(id = R.drawable.computervideo), Color(0xFFFF7043), Color(0xFFF4511E)),
        SubjectCategory("Current Affairs", painterResource(id = R.drawable.cavideo), Color(0xFF26A69A), Color(0xFF00897B))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Subject Category", fontWeight = FontWeight.SemiBold) },
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
                .background(Color(0xFFF5F5F5)) // Light Grey Background (Matches Dashboard)
                .padding(16.dp)
        ) {
            // Header Section
            Text(
                "Free Video Lectures",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                "Learn Each Subject to Watch Videos.",
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
                items(subjectList) { subject ->
                    SubjectCard(subject = subject, onClick = { onSubjectSelected(subject.name) })
                }
            }
        }
    }
}

@Composable
fun SubjectCard(subject: SubjectCategory, onClick: () -> Unit) {
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
                        colors = listOf(subject.colorStart, subject.colorEnd)
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
                            painter = subject.icon,
                            contentDescription = null,
                            tint = Color.Unspecified, // Shows original logo colors
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Text
                Text(
                    text = subject.name,
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