package com.ecosathi.gramasuvidha

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 🔥 Firebase Init
        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val signupText = findViewById<TextView>(R.id.signupText)

        // 🔑 LOGIN BUTTON
        loginBtn.setOnClickListener {

            val e = email.text.toString().trim()
            val p = password.text.toString().trim()

            // ✅ Basic validation
            if (e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Enter email & password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🔐 Firebase Login
            auth.signInWithEmailAndPassword(e, p)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val user = auth.currentUser

                        // ✅ Check email verified
                        if (user?.isEmailVerified == true) {

                            Toast.makeText(this, "Login Success ✅", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()

                        } else {

                            Toast.makeText(
                                this,
                                "Please verify your email first 📩",
                                Toast.LENGTH_LONG
                            ).show()

                            auth.signOut()
                        }

                    } else {

                        val exception = task.exception

                        when (exception) {

                            // ❌ USER NOT FOUND
                            is FirebaseAuthInvalidUserException -> {
                                Toast.makeText(
                                    this,
                                    "No account found ❌\nPlease create account first",
                                    Toast.LENGTH_LONG
                                ).show()

                                // 👉 Redirect to signup
                                startActivity(Intent(this, SignupActivity::class.java))
                            }

                            // ❌ WRONG PASSWORD
                            is FirebaseAuthInvalidCredentialsException -> {
                                Toast.makeText(
                                    this,
                                    "Wrong email or password ❌",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            // ❌ OTHER ERRORS
                            else -> {
                                Toast.makeText(
                                    this,
                                    exception?.message ?: "Login Failed ❌",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
        }

        // 🆕 OPEN SIGNUP
        signupText.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}