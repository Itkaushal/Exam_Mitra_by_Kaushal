package com.example.exammitrabykaushal.UIScreens

import com.example.exammitrabykaushal.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


// ---------------- DASHBOARD SCREEN ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onNavigateToTest: (String) -> Unit,onNavigateToProfile: () -> Unit) {

    val context = LocalContext.current
    // Get User Name from Session Manager
    var userName by remember { mutableStateOf(SessionManager.getUserName(context)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Hello, ${userName}", style = MaterialTheme.typography.titleMedium)
                        Text("Let's crack the exam! 📖",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red.copy(alpha = 0.8f))
                    }
                },
                actions = {
                    // Streak Badge
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                            Icon(painter = painterResource(id =R.drawable.streak_icon), contentDescription = null,
                                tint = Color.Red, modifier = Modifier.size(20.dp))
                            Text(" 12 Day Streak", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.Red.copy(alpha = 0.6f),
                            modifier = Modifier.size(35.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally // Center the logo
        ) {

            // ADD THE LOGO HERE
            Spacer(modifier = Modifier.height(20.dp))
            ExamMitraLogo()
            Spacer(modifier = Modifier.height(20.dp))
            // Target Goal
            Text("Daily Goal Target", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("35/50 Questions")
                        Text("70%")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(progress = 0.7f, modifier = Modifier.fillMaxWidth()
                        .height(8.dp).clip(RoundedCornerShape(4.dp)))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Features Grid
            Text("Start Practicing", style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                FeatureCard("Mock Test",
                    icon = painterResource(id = R.drawable.mock_test),
                    Color(0xFFE3F2FD)) { onNavigateToTest("Mock") }
                FeatureCard("Math Test",
                    icon = painterResource(id = R.drawable.math_test),
                    Color(0xFFE8F5E9)) { onNavigateToTest("Math") }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                FeatureCard("Reasoning",
                    icon = painterResource(id = R.drawable.reasoning_test),
                    Color(0xFFFFF3E0)) { onNavigateToTest("Reasoning") }
                FeatureCard("General Studies",
                    icon = painterResource(id = R.drawable.gkgs_test),
                    Color(0xFFF3E5F5)) { onNavigateToTest("GS") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // PYQ Section
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onNavigateToTest("PYQ") },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.pyq_icons)
                        , contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(60.dp))

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text("Previous Year Papers", fontWeight = FontWeight.Bold)
                        Text("SSC , Railway, Police, Banking, State exams...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureCard(title: String, icon: Painter, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(110.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(painter = icon, contentDescription = null, modifier = Modifier.size(32.dp), tint = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Medium, color = Color.Black)
        }
    }
}
