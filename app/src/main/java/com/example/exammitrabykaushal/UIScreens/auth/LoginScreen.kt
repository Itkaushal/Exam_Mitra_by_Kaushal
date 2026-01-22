package com.example.exammitrabykaushal.UIScreens.auth

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exammitrabykaushal.UIScreens.session.SessionManager
import com.example.exammitrabykaushal.ViewModel.LoginViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToPhone: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // 1. Google Auth Logic Setup
    val googleAuthClient = remember { GoogleAuthClient(context) }
    val viewModel: LoginViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    // 2. Launcher for Google Sign-In Intent
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    )
    { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            scope.launch {
                val signInResult = googleAuthClient.signInWithIntent(
                    intent = result.data ?: return@launch
                )
                viewModel.onSignInResult(signInResult)
            }
        }
    }

    // 3. Handle Success/Failure
    LaunchedEffect(state) {

        val user = state.signInResult?.data

        if (state.isSignInSuccessful && user != null) {

            SessionManager.saveUserName(
                context,
                user.username ?: "Google User"
            )

            SessionManager.saveUserPhoto(
                context,
                user.profilePictureUrl ?: ""
            )

            SessionManager.setLoggedIn(context, true)

            onLoginSuccess()

            viewModel.resetState()
        }
    }





    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }


    // Theme Colors
    val BluePrimary = Color(0xFF1565C0)
    val SaffronAccent = Color(0xFFFF9800)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Clean white background
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        // --- 1. BRANDING (Matching Sign Up & Dashboard) ---
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = BluePrimary,
            modifier = Modifier.size(70.dp)
        )

        // "Exam Mitra" Colored Text
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = BluePrimary, fontWeight = FontWeight.Bold)) {
                    append("Exam ")
                }
                withStyle(style = SpanStyle(color = SaffronAccent, fontWeight = FontWeight.Bold)) {
                    append("Mitra")
                }
            },
            fontSize = 32.sp
        )

        Text(
            text = "Welcome back, Aspirant!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // --- 2. EMAIL FIELD ---
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = BluePrimary) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BluePrimary,
                focusedLabelColor = BluePrimary,
                cursorColor = BluePrimary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- 3. PASSWORD FIELD ---
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BluePrimary) },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    // FIXED ICON: Uses Eye (Visibility) instead of Done/Close
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Create else Icons.Default.Close,
                        contentDescription = "Toggle Password"
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BluePrimary,
                focusedLabelColor = BluePrimary,
                cursorColor = BluePrimary
            ),
            singleLine = true
        )

        // Forgot Password Link
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            TextButton(onClick = { /* Handle Forgot Password */ }) {
                Text("Forgot Password?", color = BluePrimary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 4. LOGIN BUTTON ---
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {

                    SessionManager.saveUserName(
                        context,
                        email.substringBefore("@")
                    )

                    SessionManager.saveUserPhoto(
                        context,
                        ""
                    )

                    SessionManager.setLoggedIn(context, true)

                    onLoginSuccess()

                }else {
                    Toast.makeText(
                        context,
                        "Please enter email & password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary) // Blue Background
        ) {
            Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- 5. DIVIDER ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
            Text(
                "  Or continue with  ",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 6. SOCIAL BUTTONS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SocialButton(
                text = "Google",
                icon = Icons.Default.AccountCircle, // Using Globe as generic "Web/Google" icon
                modifier = Modifier.weight(1f),
                onClick = {
                    // 4. Trigger Google Sign In Logic
                    scope.launch {
                        val sender = googleAuthClient.signIn()
                        if (sender != null) {
                            launcher.launch(
                                IntentSenderRequest.Builder(sender).build()
                            )
                        } else {
                            Toast.makeText(context, "Google Sign In Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
            SocialButton(
                text = "Phone",
                icon = Icons.Default.Phone,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToPhone() } // <--- Call the function here,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- 7. FOOTER (Sign Up) ---
        Row(
            modifier = Modifier.padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an account? ", color = Color.Gray)
            Text(
                "Sign Up",
                color = BluePrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToSignUp() }
            )
        }
    }
}

@Composable
fun SocialButton(text: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontWeight = FontWeight.SemiBold)
        }
    }
}

