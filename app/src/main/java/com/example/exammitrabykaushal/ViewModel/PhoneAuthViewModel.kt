package com.example.exammitrabykaushal.ViewModel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.exammitrabykaushal.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit

class PhoneAuthViewModel : ViewModel() {
    private val auth = Firebase.auth

    // States
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _verificationId = MutableStateFlow<String?>(null)

    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    val verificationId = _verificationId.asStateFlow()

    private val _isCodeSent = MutableStateFlow(false)
    val isCodeSent = _isCodeSent.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // 1. Send OTP
    fun sendOtp(phoneNumber: String, activity: Activity) {
        _isLoading.value = true
        _errorMessage.value = null

        // Auto-add country code if missing (Assuming India +91 for this example)
        val fullNumber = if (phoneNumber.startsWith("+")) phoneNumber else "+91$phoneNumber"

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(fullNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto-verification (Instant login on some devices)
                    signInWithCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _isLoading.value = false
                    _errorMessage.value = e.message
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    _isLoading.value = false
                    _verificationId.value = verificationId
                    _isCodeSent.value = true // Triggers UI to show OTP box
                    resendToken = token
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // 2. Verify OTP
    fun verifyOtp(code: String) {
        val vid = _verificationId.value ?: return
        if (code.length < 6) {
            _errorMessage.value = "Please enter a 6-digit code"
            return
        }
        _isLoading.value = true
        val credential = PhoneAuthProvider.getCredential(vid, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _loginSuccess.value = true
                } else {
                    _errorMessage.value = task.exception?.message ?: "Verification failed"
                }
            }
    }

    fun resendOtp(phoneNumber: String, activity: Activity) {
        _isLoading.value = true
        _errorMessage.value = null

        // Auto-add country code if missing (Assuming India +91 for this example)
        val fullNumber = if (phoneNumber.startsWith("+")) phoneNumber else "+91$phoneNumber"

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(fullNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto-verification (Instant login on some devices)
                    signInWithCredentialResend(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _isLoading.value = false
                    _errorMessage.value = e.message
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    _isLoading.value = false
                    _verificationId.value = verificationId
                    resendToken = token // Update token for next time
                }
            })
            .setForceResendingToken(resendToken!!) // <--- CRITICAL: Pass the token here
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyresendOtp(code: String) {
        val vid = _verificationId.value ?: return
        if (code.length < 6) {
            _errorMessage.value = "Please enter a 6-digit code"
            return
        }
        _isLoading.value = true
        val credential = PhoneAuthProvider.getCredential(vid, code)
        signInWithCredentialResend(credential)
    }

    private fun signInWithCredentialResend(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _loginSuccess.value = true
                } else {
                    _errorMessage.value = task.exception?.message ?: "Verification failed"
                }
            }
    }

}
