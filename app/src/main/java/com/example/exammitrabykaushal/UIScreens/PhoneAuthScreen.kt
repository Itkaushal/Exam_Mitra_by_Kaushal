package com.example.exammitrabykaushal.UIScreens


import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exammitrabykaushal.ViewModel.PhoneAuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneAuthScreen(
    onLoginSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val viewModel: PhoneAuthViewModel = viewModel()

    // State from ViewModel
    val isCodeSent by viewModel.isCodeSent.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Local Input State
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }

    // Theme Color
    val BluePrimary = Color(0xFF1565C0)

    // Handle Success
    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            SessionManager.setLoggedIn(context, true)
            // Optional: Save user phone as name if name is empty
            SessionManager.saveUserName(context, "User $phoneNumber")
            onLoginSuccess()
        }
    }

    // Handle Error Toast
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isCodeSent) "Verify OTP" else "Phone Login") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            if (!isCodeSent) {
                // --- STEP 1: ENTER PHONE NUMBER ---
                Text("Enter Mobile Number",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold)

                Text("We will send you a confirmation code",
                    color = Color.Gray)

                Spacer(modifier = Modifier.height(30.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { if (it.length <= 10) phoneNumber = it },
                    label = { Text("Mobile Number") },
                    prefix = { Text("+91 ", fontWeight = FontWeight.Bold) }, // Country Code
                    leadingIcon = { Icon(Icons.Default.Phone, null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        focusedLabelColor = BluePrimary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (phoneNumber.length == 10) {
                            activity?.let { viewModel.sendOtp(phoneNumber, it) }
                        } else {
                            Toast.makeText(context, "Enter valid 10-digit number", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    enabled = !isLoading
                ) {
                    if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("Get OTP", fontSize = 18.sp)
                }
            } else {
                // --- STEP 2: ENTER OTP ---
                Text("Verification Code", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Please enter the code sent to +91 $phoneNumber", color = Color.Gray, textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(30.dp))

                // CUSTOM OTP BOX UI
                OtpInputField(
                    otpText = otpCode,
                    onOtpChange = { if (it.length <= 6) otpCode = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.verifyOtp(otpCode) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    enabled = !isLoading
                ) {
                    if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("Verify & Login", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = {
                        activity?.let { act ->
                            // Call the new resend function
                            viewModel.resendOtp(phoneNumber, act)
                            Toast.makeText(context, "Code sent again!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    // Disable button if loading to prevent spam clicks
                    enabled = !isLoading
                ) {
                    Text(
                        text = if (isLoading) "Sending..." else "Didn't receive code? Resend",
                        color = BluePrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// --- CUSTOM OTP BOX UI ---
@Composable
fun OtpInputField(otpText: String, onOtpChange: (String) -> Unit) {
    BasicTextField(
        value = otpText,
        onValueChange = onOtpChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                repeat(6) { index ->
                    val char = when {
                        index >= otpText.length -> ""
                        else -> otpText[index].toString()
                    }
                    val isFocused = otpText.length == index

                    Box(
                        modifier = Modifier
                            .width(45.dp)
                            .height(50.dp)
                            .border(
                                width = if (isFocused) 2.dp else 1.dp,
                                color = if (isFocused) Color(0xFF1565C0) else Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(Color.White, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    )
}