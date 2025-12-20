package com.example.exammitrabykaushal.UIScreens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit, // New callback for logging out
    onNavigateToHistory: () -> Unit,
    onNavigateToNotification: () -> Unit
) {
    val context = LocalContext.current
    var userName by remember { mutableStateOf(SessionManager.getUserName(context)) }
    val userPhoto = SessionManager.getUserPhoto(context)

    // State for Edit Dialog
    var showEditDialog by remember { mutableStateOf(false) }

    // If Edit Dialog is open
    if (showEditDialog) {
        EditProfileDialog(
            currentName = userName,
            onDismiss = { showEditDialog = false },
            onSave = { newName ->
                // Save to Session and Update UI
                SessionManager.saveUserName(context, newName)
                userName = newName
                showEditDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // User Profile section--------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF1565C0), Color(0xFF42A5F5))
                        ),
                        shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
                    )
                    .padding(bottom = 32.dp, top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // User Profile Image----------------------------
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(3.dp, Color(0xFFFF9800))
                    ) {
                        if (userPhoto != null) {
                            AsyncImage(
                                model = userPhoto,
                                contentDescription = "Profile Image",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else{
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Display User Name-----------------
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Serif

                    )
                    Text(
                        text = "SSC CGL • Target 2024",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // --- SECTION 2: STATS ---
            PaddingValues(horizontal = 16.dp)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-20).dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatItem(Icons.Default.Face, Color(0xFFFFD700), "#1,420", "Rank")
                    Divider(modifier = Modifier.height(40.dp).width(1.dp))
                    StatItem(Icons.Default.DateRange, Color(0xFFFF3D00), "12 Days", "Streak")
                    Divider(modifier = Modifier.height(40.dp).width(1.dp))
                    StatItem(Icons.Default.Menu, Color(0xFF4CAF50), "85%", "Accuracy")
                }
            }

            // --- SECTION 3: MENU OPTIONS ---
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Account Settings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
                )

                // 3. Edit Profile Click Action
                ProfileOptionCard(
                    icon = Icons.Default.Edit,
                    title = "Edit Profile",
                    onClick = { showEditDialog = true }
                )
                // notification click action
                ProfileOptionCard(icon = Icons.Default.Notifications, title = "Notifications", onClick = {onNavigateToNotification()})
                // test history click action
                ProfileOptionCard(icon = Icons.Default.HistoryToggleOff, title = "Test History", onClick = {onNavigateToHistory()})

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Logout Click Action
                ProfileOptionCard(
                    icon = Icons.Default.Logout,
                    title = "Logout",
                    isDestructive = true,
                    onClick = {
                        // Clear Session
                        SessionManager.setLoggedIn(context, false)
                        // Navigate to Login
                        onLogout()
                    }
                )
            }
        }
    }
}

// --- HELPER COMPONENTS ---

@Composable
fun StatItem(icon: ImageVector, color: Color, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

@Composable
fun ProfileOptionCard(icon: ImageVector, title: String, isDestructive: Boolean = false, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }, // Added Clickable
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDestructive) Color.Red else Color(0xFF1565C0)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDestructive) Color.Red else Color.Black,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}

// --- NEW EDIT DIALOG ---
@Composable
fun EditProfileDialog(currentName: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var text by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = AlertDialogDefaults.shape,
        icon = { Icons.Default.Edit},
        title = { Text("Edit Profile", fontWeight = FontWeight.W700, fontFamily = FontFamily.Serif) },
        text = {
            Column {
                Text("Update your display name:", color = Color.Blue)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = Color.Blue.copy(alpha = 0.5f),
                        unfocusedIndicatorColor = Color.Blue.copy(alpha = 0.5f),
                        focusedTextColor = Color.Blue,
                        focusedIndicatorColor = Color.Blue,
                        focusedPlaceholderColor = Color.Blue,
                        unfocusedPlaceholderColor = Color.Blue.copy(0.5f)
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(text) },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color.Cyan.copy(alpha = 0.4f)
                ),
                border = BorderStroke(1.dp, color = Color.Black)

                ) {
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