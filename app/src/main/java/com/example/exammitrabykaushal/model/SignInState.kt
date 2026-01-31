package com.example.exammitrabykaushal.model

import com.example.exammitrabykaushal.UIScreens.auth.SignInResult

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInResult: SignInResult? = null,
    val signInError: String? = null
)