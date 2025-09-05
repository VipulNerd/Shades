package com.example.shades.authentication

import AppUser
import androidx.lifecycle.ViewModel
import com.example.shades.utils.generateRandomName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersRef = firestore.collection("users")

    private val _currentUser = MutableStateFlow<AppUser?>(null)
    val currentUser: StateFlow<AppUser?> = _currentUser

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _signupSuccess = MutableStateFlow(false)
    val signupSuccess: StateFlow<Boolean> = _signupSuccess

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    init {
        auth.currentUser?.uid?.let { uid ->
            loadUser(uid)
        }
    }

    fun signUp(email: String, password: String) {
        if (!email.endsWith("@vitbhopal.ac.in")) {
            _error.value = "Only VIT Bhopal emails are allowed"
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = auth.currentUser!!.uid
                val user = AppUser(
                    uid = uid,
                    email = email,
                    displayName = generateRandomName()
                )

                usersRef.document(uid).set(user)
                    .addOnSuccessListener {
                        _currentUser.value = user
                        _signupSuccess.value = true
                    }
                    .addOnFailureListener { e -> _error.value = e.message }
            }
            .addOnFailureListener { e -> _error.value = e.message }
    }

    fun login(email: String, password: String) {
        if (!email.endsWith("@vitbhopal.ac.in")) {
            _error.value = "Only VIT Bhopal emails are allowed"
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = auth.currentUser!!.uid
                loadUser(uid) {
                    _loginSuccess.value = true
                }
            }
            .addOnFailureListener { e -> _error.value = e.message }
    }

    private fun loadUser(uid: String, onLoaded: (() -> Unit)? = null) {
        usersRef.document(uid).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.toObject(AppUser::class.java)
                if (user != null) {
                    _currentUser.value = user
                    onLoaded?.invoke()
                } else {
                    _error.value = "User record missing"
                }
            }
            .addOnFailureListener { e -> _error.value = e.message }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
        _signupSuccess.value = false
        _loginSuccess.value = false
    }
}