package edu.utap.photolist

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlin.system.exitProcess

// required to avoid crash on Android 12 API 31

// https://firebase.google.com/docs/auth/android/firebaseui
class AuthInit(viewModel: MainViewModel, signInLauncher: ActivityResultLauncher<Intent>) {
    companion object {
        private const val TAG = "AuthInit"
    }

    init {
        val user = FirebaseAuth.getInstance().currentUser
        if(user == null) {

            Log.d(TAG, "XXX user null")
            // Choose authentication providers
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build())
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
            signInLauncher.launch(signInIntent)
            // Create and launch sign-in intent
            // XXX Write me. Set authentication providers and start sign-in for user
            // setIsSmartLockEnabled(false) solves some problems
        } else {
            Log.d(TAG, "XXX user ${user.displayName} email ${user.email} uid ${user.uid}")
            viewModel.updateUser()
        }
    }
}
