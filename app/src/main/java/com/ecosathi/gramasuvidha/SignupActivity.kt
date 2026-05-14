package com.ecosathi.gramasuvidha

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val nameField = findViewById<EditText>(R.id.name)
        val addressField = findViewById<EditText>(R.id.address)
        val emailField = findViewById<EditText>(R.id.email)
        val passwordField = findViewById<EditText>(R.id.password)

        val signupBtn = findViewById<Button>(R.id.signupBtn)
        val resendBtn = findViewById<Button>(R.id.resendBtn)
        val checkVerifyBtn = findViewById<Button>(R.id.checkVerifyBtn)

        // 🔐 CREATE ACCOUNT
        signupBtn.setOnClickListener {

            val name = nameField.text.toString().trim()
            val address = addressField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            // ✅ Validation
            if (name.isEmpty() || address.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🔐 Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    if (it.isSuccessful) {

                        val user = auth.currentUser!!
                        val uid = user.uid

                        // 🔥 SAVE USER DATA (IMPORTANT)
                        val userData = HashMap<String, String>()
                        userData["name"] = name
                        userData["address"] = address
                        userData["email"] = email

                        FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .setValue(userData)
                            .addOnSuccessListener {

                                // 📩 SEND VERIFICATION EMAIL
                                user.sendEmailVerification()
                                    .addOnSuccessListener {

                                        AlertDialog.Builder(this)
                                            .setTitle("Verify Your Email 📩")
                                            .setMessage(
                                                "Account created successfully.\n\n" +
                                                        "Check your Gmail to verify your account.\n" +
                                                        "Also check Spam folder.\n\n" +
                                                        "After verifying, click 'I Have Verified'."
                                            )
                                            .setPositiveButton("OK", null)
                                            .show()
                                    }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to save user data ❌", Toast.LENGTH_SHORT).show()
                            }

                    } else {
                        Toast.makeText(
                            this,
                            it.exception?.message ?: "Signup Failed ❌",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // 🔁 RESEND VERIFICATION EMAIL
        resendBtn.setOnClickListener {

            val user = auth.currentUser

            if (user != null) {
                user.sendEmailVerification()
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Verification email sent again 📩\nCheck Gmail / Spam",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            } else {
                Toast.makeText(this, "Create account first", Toast.LENGTH_SHORT).show()
            }
        }

        // ✅ CHECK IF VERIFIED
        checkVerifyBtn.setOnClickListener {

            val user = auth.currentUser

            if (user != null) {

                user.reload().addOnCompleteListener {

                    if (user.isEmailVerified) {

                        Toast.makeText(
                            this,
                            "You are verified ✅",
                            Toast.LENGTH_LONG
                        ).show()

                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()

                    } else {

                        Toast.makeText(
                            this,
                            "Not verified yet ❌\nCheck Gmail / Spam",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } else {
                Toast.makeText(this, "Create account first", Toast.LENGTH_SHORT).show()
            }
        }
    }
}