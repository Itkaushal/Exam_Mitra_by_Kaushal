package com.example.exammitrabykaushal.UIScreens.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.example.exammitrabykaushal.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class GoogleAuthClient(private val context: Context) {

    private val auth = Firebase.auth
    private val oneTapClient: SignInClient = Identity.getSignInClient(context)

    suspend fun signIn(): IntentSender? {
        return try {
            val result = oneTapClient.beginSignIn(
                BeginSignInRequest.builder()
                    .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            .setFilterByAuthorizedAccounts(false)
                            .setServerClientId(
                                context.getString(R.string.default_web_client_id)
                            )
                            .build()
                    )
                    .setAutoSelectEnabled(true)
                    .build()
            ).await()

            result.pendingIntent.intentSender
        } catch (e: Exception) {
            Log.e("GoogleAuthClient", "Google Sign-In failed", e)
            null
        }
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user

            Log.d("GoogleAuthClient", "Firebase user: ${user?.displayName}")
            Log.d("GoogleAuthClient", "Photo: ${user?.photoUrl}")

            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            SignInResult(null, e.message)
        }
    }
}

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)
