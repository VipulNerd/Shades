
import androidx.lifecycle.ViewModel
import com.example.shades.utils.generateRandomName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    private val _currentUser = MutableStateFlow<AppUser?>(null)
    val currentUser: StateFlow<AppUser?> = _currentUser

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Flags to trigger navigation immediately
    private val _signupSuccess = MutableStateFlow(false)
    val signupSuccess: StateFlow<Boolean> = _signupSuccess

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    init {
        // If already logged in, fetch user info
        auth.currentUser?.uid?.let { uid ->
            fetchUser(uid)
        }
    }

    fun signUp(email: String, password: String) {
        if (!email.endsWith("@vitbhopal.ac.in")) {
            _error.value = "Only VIT Bhopal emails are allowed"
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val randomName = generateRandomName()

                    val user = AppUser(uid = uid, email = email, displayName = randomName)

                    // Set success flag immediately
                    _signupSuccess.value = true
                    _currentUser.value = user

                    db.child("users").child(uid).setValue(user)
                        .addOnFailureListener { e ->
                            _error.value = e.message
                        }
                } else {
                    _error.value = task.exception?.message
                }
            }
    }

    fun login(email: String, password: String) {
        if (!email.endsWith("@vitbhopal.ac.in")) {
            _error.value = "Only VIT Bhopal emails are allowed"
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginSuccess.value = true  // Trigger navigation immediately
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    fetchUser(uid) // Still fetch user data in background
                } else {
                    _error.value = task.exception?.message
                }
            }
    }

    private fun fetchUser(uid: String) {
        db.child("users").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(AppUser::class.java)
                    if (user != null) {
                        _currentUser.value = user
                    } else {
                        _error.value = "User data not found"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _error.value = error.message
                }
            })
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
        _signupSuccess.value = false
        _loginSuccess.value = false
    }
}