package com.example.exammitrabykaushal.ViewModel

import androidx.lifecycle.ViewModel
import com.example.exammitrabykaushal.UIScreens.auth.SignInResult
import com.example.exammitrabykaushal.model.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.value = SignInState(
            isSignInSuccessful = result.data != null,
            signInResult = result,
            signInError = result.errorMessage
        )
    }

    fun resetState() {
        _state.value = SignInState()
    }
}
