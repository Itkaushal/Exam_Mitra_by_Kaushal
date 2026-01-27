package com.example.exammitrabykaushal.UIScreens.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.exammitrabykaushal.UIScreens.screen.chart_scrren.AccuracyPieChart
import com.example.exammitrabykaushal.UIScreens.screen.chart_scrren.MiniPerformanceChart
import com.example.exammitrabykaushal.UIScreens.session.SessionManager
import com.example.exammitrabykaushal.ViewModel.TestHistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToNotification: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: TestHistoryViewModel = viewModel()
    val stats by viewModel.profileStats.collectAsState()
    val history by viewModel.historyList.collectAsState()


    var userName by rememberSaveable { mutableStateOf("Guest User") }
    var userPhoto by rememberSaveable { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        userName = SessionManager.getUserName(context).ifBlank { "Guest User" }
        userPhoto = SessionManager.getUserPhoto(context)

        Log.d("ProfileScreen", "Name: $userName")
        Log.d("ProfileScreen", "Photo: $userPhoto")
    }

    if (showEditDialog) {
        EditProfileDialog(
            currentName = userName,
            onDismiss = { showEditDialog = false },
            onSave = {
                SessionManager.saveUserName(context, it)
                userName = it
                showEditDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
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

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF4F6FA))
        ) {

            /* ---------------- HEADER ---------------- */

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF1565C0),
                                Color(0xFF1E88E5)
                            )
                        ),
                        RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .padding(vertical = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Surface(
                        modifier = Modifier
                            .size(96.dp)
                            .shadow(6.dp, CircleShape),
                        shape = CircleShape,
                        color = Color.White,
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        if (userPhoto.isNotBlank()) {
                            AsyncImage(
                                model = userPhoto,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                Icons.Default.Person,
                                null,
                                tint = Color.Gray,
                                modifier = Modifier.padding(22.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        userName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        "SSC • Banking • Railway",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            /* ---------------- STATS ---------------- */

            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = (-20).dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier
                    .padding(16.dp)
                    .padding(start = 8.dp, end = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(Icons.Default.EmojiEvents, "${stats.rank}", "Rank")
                        StatItem(Icons.Default.LocalFireDepartment, "${stats.streakDays}", "Streak")
                        StatItem(Icons.Default.TaskAlt, "${stats.accuracy}%", "Accuracy")
                    }

                    Spacer(Modifier.height(12.dp))

                    MiniPerformanceChart(history)
                }
            }

            Spacer(Modifier.height(12.dp))

            ChartCard(title = "OverAll Accuracy") {
                AccuracyPieChart(historyList = history)
            }

            Spacer(Modifier.height(8.dp))

            /* ---------------- OPTIONS ---------------- */

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                SectionTitle("Account")

                ProfileOptionCard(Icons.Default.Edit, "Edit Profile") {
                    showEditDialog = true
                }

                ProfileOptionCard(Icons.Default.Notifications, "Notifications") {
                    onNavigateToNotification()
                }

                ProfileOptionCard(Icons.Default.History, "Test History") {
                    onNavigateToHistory()
                }

                Spacer(Modifier.height(16.dp))

                SectionTitle("Danger Zone")

                ProfileOptionCard(
                    icon = Icons.Default.Logout,
                    title = "Logout",
                    isDestructive = true
                ) {
                    SessionManager.clearSession(context)
                    onLogout()
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

/* ---------------- COMPONENTS ---------------- */

@Composable
fun SectionTitle(title: String) {
    Text(
        title.uppercase(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun StatItem(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            null,
            tint = Color(0xFF1565C0),
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(value, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun ProfileOptionCard(
    icon: ImageVector,
    title: String,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                null,
                tint = if (isDestructive) Color.Red else Color(0xFF1565C0)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                title,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Medium,
                color = if (isDestructive) Color.Red else Color.Black
            )
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}

/* ---------------- EDIT DIALOG ---------------- */

@Composable
fun EditProfileDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                label = { Text("Display Name") }
            )
        },
        confirmButton = {
            Button(onClick = { onSave(text) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
